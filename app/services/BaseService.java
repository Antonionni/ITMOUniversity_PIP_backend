package services;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.function.Function;

public abstract class BaseService {
    protected final JPAApi JpaApi;

    @Inject
    public BaseService(JPAApi jpaApi) {
        this.JpaApi = jpaApi;
    }

    protected <T> T wrap(Function<EntityManager, T> function) {
        return JpaApi.withTransaction(function);
    }
}
