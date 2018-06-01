package data;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HibernateUtils {

    private final JPAApi JpaApi;

    @Inject
    HibernateUtils(JPAApi jpaApi) {
        JpaApi = jpaApi;
    }

    public EntityManager getEntityManager() {
        return JpaApi.em();
    }
}
