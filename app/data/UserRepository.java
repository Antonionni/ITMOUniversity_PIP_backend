package data;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import models.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void shutdown() {
        getSessionFactory().close();
    }

}
