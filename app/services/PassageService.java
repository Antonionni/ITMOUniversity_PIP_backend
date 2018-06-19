package services;

import Exceptions.BusinessException;
import Exceptions.NoCurrentPassageException;
import Exceptions.UserHasAlreadyInPassageException;
import Exceptions.WrongAmountOfAnswersException;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import enumerations.AnswerType;
import models.entities.*;
import models.serviceEntities.LessonPage;
import models.serviceEntities.Passage;
import models.serviceEntities.PassageItem;
import models.serviceEntities.VerifyPassageItemModel;
import org.jetbrains.annotations.NotNull;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;
import scala.concurrent.duration.FiniteDuration;
import telegram_rabbit.PassageActor;
import telegram_rabbit.PassageActorCreator;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static akka.pattern.Patterns.*;

public class PassageService extends BaseService implements IPassageService {
    private final IUserService UserService;
    private final ActorSystem system;
    private final PassageActorCreator creator;

    @Inject
    public PassageService(JPAApi jpaApi, HttpExecutionContext ec, IUserService userService, ActorSystem system, PassageActorCreator creator) {
        super(jpaApi, ec);
        this.UserService = userService;
        this.system = system;
        this.creator = creator;
    }

    @Override
    public CompletionStage<Collection<PassageItem>> listNeedToVerifiedPassageItems(int id) {
        return supplyAsync(() -> wrap(em -> {
            PassageEntity passageEntity = em.find(PassageEntity.class, id);
            if (passageEntity == null) {
                throw new BusinessException();
            }
            return passageEntity.getPassages()
                    .stream()
                    .filter(x -> x.getQuestionEntity().getAnswerType() == AnswerType.TextArea)
                    .map(PassageItem::new).collect(Collectors.toList());
        }), ec.current());
    }

    @Override
    public CompletionStage<Collection<PassageItem>> savePassageItem(Collection<PassageItem> items) {
        return supplyAsync(() -> wrap(em -> {
            UserEntity userEntity = UserService.getCurrentUser();
            if (userEntity == null) {
                throw new BusinessException();
            }
            PassageEntity passageEntity = getUserCurrentPassage(userEntity);
            if (passageEntity == null) {
                throw new NoCurrentPassageException();
            }
            List<PassageItem> passageItems = items.stream().map(x -> {
                QuestionEntity questionEntity = em.find(QuestionEntity.class, x.getQuestionId());
                if (questionEntity == null) {
                    throw new BusinessException();
                }
                PassageHasAnswersEntity passageHasAnswersEntity = createPassageItem(x, em, passageEntity, questionEntity);
                em.persist(passageHasAnswersEntity);
                return passageHasAnswersEntity;
            })
                    .map(PassageItem::new)
                    .collect(Collectors.toList());
            em.merge(closePassage(passageEntity));
            return passageItems;
        }), ec.current());
    }

    @NotNull
    private PassageHasAnswersEntity createPassageItem(PassageItem item, EntityManager em, PassageEntity passageEntity, QuestionEntity questionEntity) {
        PassageHasAnswersEntity passageHasAnswersEntity = new PassageHasAnswersEntity();
        passageHasAnswersEntity.setAnswers(item.getAnswerIds().stream().map(x -> em.find(AnswerEntity.class, x)).collect(Collectors.toList()));
        passageHasAnswersEntity.setPassage(passageEntity);
        passageHasAnswersEntity.setQuestionEntity(questionEntity);
        passageHasAnswersEntity.setStartdate(new Date());
        passageHasAnswersEntity.setTextAnswer(item.getTextAnswer());
        passageHasAnswersEntity.setVerifiedByTeacher(item.getVerified());
        return passageHasAnswersEntity;
    }

    @Override
    public CompletionStage<PassageItem> verifyPassageItem(VerifyPassageItemModel model) {
        return supplyAsync(() -> wrap(em -> {
            PassageHasAnswersEntity passageHasAnswersEntity = em.find(PassageHasAnswersEntity.class, model.getPassageId());
            if (passageHasAnswersEntity == null) {
                throw new BusinessException();
            }
            passageHasAnswersEntity.setVerifiedByTeacher(model.isResult());
            return new PassageItem(passageHasAnswersEntity);
        }), ec.current());
    }

    @Override
    public CompletionStage<LessonPage> startPassage(int testId) {
        return supplyAsync(() -> wrap(em -> {
            UserEntity userEntity = UserService.getCurrentUser();
            if (getUserCurrentPassage(userEntity) != null) {
                throw new UserHasAlreadyInPassageException();
            }
            TestEntity testEntity = em.find(TestEntity.class, testId);
            if (testEntity == null) {
                throw new BusinessException();
            }
            PassageEntity passageEntity = startPassage(testEntity, userEntity);
            em.persist(passageEntity);
            ActorRef passageActor = system.actorOf(Props.create(creator));
            system
                    .scheduler()
                    .scheduleOnce(
                            FiniteDuration.apply(testEntity.getMinutesToGo(), TimeUnit.MINUTES),
                            passageActor,
                            passageEntity,
                            system.dispatcher(),
                            null);
            return new LessonPage(testEntity);
        }), ec.current());
    }

    @Override
    public CompletableFuture<Passage> closePassage() {
        return supplyAsync(() -> wrap(em -> {
            UserEntity userEntity = UserService.getCurrentUser();
            PassageEntity passageEntity = getUserCurrentPassage(userEntity);
            if (passageEntity == null) {
                throw new NoCurrentPassageException();
            }
            return new Passage(closePassage(passageEntity));
        }), ec.current());
    }

    private PassageEntity startPassage(TestEntity testEntity, UserEntity userEntity) {
        PassageEntity passageEntity = new PassageEntity();
        passageEntity.setStartdate(new Date());
        passageEntity.setTest(testEntity);
        passageEntity.setUser(userEntity);
        return passageEntity;
    }

    public PassageEntity closePassage(PassageEntity passageEntity) {
        passageEntity.setEnddate(new Date());
        Collection<PassageHasAnswersEntity> passages = passageEntity.getPassages();
        TestEntity testEntity = passageEntity.getTest();
        int threshold = testEntity.getThreshold();
        int result = (int) passages.stream().map(this::CalculateResult).count();
        passageEntity.setRight(result >= threshold);
        passageEntity.setResult(result);
        return passageEntity;
    }

    private boolean CalculateResult(PassageHasAnswersEntity passage) {
        Collection<AnswerEntity> answerEntities = passage.getAnswers();
        QuestionEntity questionEntity = passage.getQuestionEntity();
        switch (questionEntity.getAnswerType()) {
            case CheckBox:
                if (answerEntities.stream().allMatch(AnswerEntity::isRight)) {
                    return true;
                }
                break;
            case RadioButton:
                if (answerEntities.size() != 1) {
                    throw new WrongAmountOfAnswersException();
                }
                if (answerEntities.iterator().next().isRight()) {
                    return true;
                }
                break;
            case TextArea:
                return passage.getVerifiedByTeacher();
        }
        return false;
    }

    private PassageEntity getUserCurrentPassage(UserEntity userEntity) {
        try {
            Query query = JpaApi
                    .em()
                    .createNativeQuery("select * " +
                            "from {h-schema}passages pas " +
                                "inner join {h-schema}tests tst " +
                                "on tst.id=pas.testid " +
                            "where pas.user_id = :userId " +
                                "and (pas.startdate) < current_timestamp AT TIME ZONE 'UTC' " +
                                "and (pas.startdate +  make_interval(0,0,0,0,0,tst.minutestogo,0)) > current_timestamp AT TIME ZONE 'UTC'",
                            PassageEntity.class);
            query.setParameter("userId", userEntity.getId());
            return (PassageEntity)query.getSingleResult();
        } catch (NoResultException ex) {
            // that's ok
            return null;
        }
    }

}
