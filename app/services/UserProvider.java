package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import models.entities.UserEntity;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class UserProvider {

    private final PlayAuthenticate auth;

    private final IUserDAO IUserDao;

    @Inject
    public UserProvider(final PlayAuthenticate auth, IUserDAO IUserDao) {
        this.auth = auth;
        this.IUserDao = IUserDao;
    }

    @Nullable
    public UserEntity getUser(Http.Session session) {
        final AuthUser currentAuthUser = this.auth.getUser(session);
        return IUserDao.findByAuthUserIdentity(currentAuthUser);
    }
}

