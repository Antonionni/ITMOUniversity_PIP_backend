package services;

import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.function.Function;

public abstract class BaseService {
    protected final JPAApi JpaApi;
    protected final HttpExecutionContext ec;

    @Inject
    public BaseService(JPAApi jpaApi, HttpExecutionContext ec) {
        this.JpaApi = jpaApi;
        this.ec = ec;
    }

    protected <T> T wrap(Function<EntityManager, T> function) {
        return JpaApi.withTransaction(function);
    }
}
