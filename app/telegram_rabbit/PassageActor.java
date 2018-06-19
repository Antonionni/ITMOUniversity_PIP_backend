package telegram_rabbit;

import Exceptions.PassageAlreadyClosedException;
import akka.actor.AbstractActor;
import akka.actor.IllegalActorStateException;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.inject.Inject;
import models.entities.PassageEntity;
import play.Logger;
import play.db.jpa.JPAApi;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import services.IPassageService;
import services.PassageService;

import javax.persistence.EntityManager;
import java.util.function.Function;

public class PassageActor extends UntypedActor {
    @Inject
    private IPassageService PassageService;

    @Inject
    private JPAApi JpaApi;

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof PassageEntity) {
            wrap(em -> {
                PassageEntity passageEntity = (PassageEntity)msg;
                passageEntity = em.find(PassageEntity.class, passageEntity.getId());
                if(passageEntity.getEnddate() != null) {
                    Logger.error("passage already closed" + passageEntity.getId());
                    throw new PassageAlreadyClosedException();
                }
                return PassageService.closePassage(passageEntity);
            });
        }
    }

    protected <T> T wrap(Function<EntityManager, T> function) {
        return JpaApi.withTransaction(function);
    }
}
