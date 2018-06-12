package services;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.function.Function;

public abstract class BaseService {
    protected final JPAApi JpaApi;
    protected final CustomExecutionContext ec;

    @Inject
    public BaseService(JPAApi jpaApi, CustomExecutionContext ec) {
        this.JpaApi = jpaApi;
        this.ec = ec;
    }

    protected <T> T wrap(Function<EntityManager, T> function) {
        return JpaApi.withTransaction(function);
    }
}
