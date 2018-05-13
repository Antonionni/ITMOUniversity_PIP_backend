package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import data.HibernateUtils;
import models.entities.UserEntity;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
public class UserProvider {

    private final PlayAuthenticate auth;

    private final UserDAO userDao;

    @Inject
    public UserProvider(final PlayAuthenticate auth, UserDAO userDao) {
        this.auth = auth;
        this.userDao = userDao;
    }

    @Nullable
    public UserEntity getUser(Http.Session session) {
        final AuthUser currentAuthUser = this.auth.getUser(session);
        return userDao.findByAuthUserIdentity(currentAuthUser);
    }
}

