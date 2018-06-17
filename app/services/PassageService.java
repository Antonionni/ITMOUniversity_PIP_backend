package services;

import Exceptions.BusinessException;
import Exceptions.NoPassageToCloseException;
import Exceptions.UserHasAlreadyInPassageException;
import Exceptions.WrongAmountOfAnswersException;
import models.entities.*;
import models.serviceEntities.LessonPage;
import models.serviceEntities.Passage;
import models.serviceEntities.PassageItem;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PassageService extends BaseService {
    private final ILessonService LessonService;
    private final IUserService UserService;

    @Inject
    public PassageService(JPAApi jpaApi, HttpExecutionContext ec, ILessonService lessonService, IUserService userService) {
        super(jpaApi, ec);
        this.LessonService = lessonService;
        this.UserService = userService;
    }

    public CompletionStage<Collection<PassageItem>> ListNeedToVerifiedPassageItems(PassageEntityPK id) {
        return supplyAsync(() -> wrap(em -> {
            PassageEntity passageEntity = em.find(PassageEntity.class, id);
            if(passageEntity == null) {
                throw new BusinessException();
            }
            return passageEntity.getPassages().stream().map(PassageItem::new).collect(Collectors.toList());
        }), ec.current());
    }

    public CompletionStage<LessonPage> startPassage(int testId) {
        return supplyAsync(() -> wrap(em -> {
            UserEntity userEntity = UserService.getCurrentUser();
            if(getUserCurrentPassage(userEntity) != null) {
                throw new UserHasAlreadyInPassageException();
            }
            TestEntity testEntity = em.find(TestEntity.class, testId);
            if(testEntity == null) {
                throw new BusinessException();
            }
            em.persist(startPassage(testEntity, userEntity));
            return new LessonPage(testEntity);
        }), ec.current());
    }

    public CompletableFuture<Passage> closePassage() {
        return supplyAsync(() -> wrap(em -> {
            UserEntity userEntity = UserService.getCurrentUser();
            PassageEntity passageEntity = getUserCurrentPassage(userEntity);
            if(passageEntity == null) {
                throw new NoPassageToCloseException();
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

    private PassageEntity closePassage(PassageEntity passageEntity) {
        passageEntity.setEnddate(new Date());
        Collection<PassageHasAnswersEntity> passages = passageEntity.getPassages();
        TestEntity testEntity = passageEntity.getTest();
        int threshold = testEntity.getThreshold();
        int result = (int) passages.stream().map(this::CalculateResult).count();
        passageEntity.setRight(result >= threshold);
        return passageEntity;
    }

    private boolean CalculateResult(PassageHasAnswersEntity passage) {
        Collection<AnswerEntity> answerEntities = passage.getAnswers();
        QuestionEntity questionEntity = passage.getQuestionEntity();
        switch(questionEntity.getAnswerType()) {
            case CheckBox:
                if(answerEntities.stream().allMatch(AnswerEntity::isRight)) {
                    return true;
                }
                break;
            case RadioButton:
                if(answerEntities.size() != 1) {
                    throw new WrongAmountOfAnswersException();
                }
                if(answerEntities.iterator().next().isRight()) {
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
            TypedQuery<PassageEntity> query = JpaApi
                    .em()
                    .createQuery("select pas from PassageEntity pas join pas.test tst where pas.user = :user and pas.startdate < current_time and (pas.startdate + tst.minutesToGo) > current_time ", PassageEntity.class);
            query.setParameter("user", userEntity);
            return query.getSingleResult();
        } catch(NoResultException ex) {
            // that's ok
            return null;
        }
    }

}
