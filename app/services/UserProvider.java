package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import models.entities.UserEntity;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class UserProvider {

    private final PlayAuthenticate auth;

    private final IUserService UserService;

    @Inject
    public UserProvider(final PlayAuthenticate auth, IUserService UserService) {
        this.auth = auth;
        this.UserService = UserService;
    }

    @Nullable
    public UserEntity getUser(Http.Session session) {
        final AuthUser currentAuthUser = this.auth.getUser(session);
        return UserService.findByAuthUserIdentity(currentAuthUser);
    }
}

