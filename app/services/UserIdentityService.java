package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import models.entities.UserEntity;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

public class UserIdentityService extends AbstractUserService {
    private IUserService UserService;

    @Inject
    public UserIdentityService(final PlayAuthenticate auth, final IUserService UserService) {
        super(auth);
        this.UserService = UserService;
    }


    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = UserService.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return UserService.create(authUser).getId();
        } else {
            // we have this user already, so return null
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity identity) {
        final UserEntity u = UserService.findByAuthUserIdentity(identity);
        if (u != null) {
            return u.getId();
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            UserService.merge(oldUser, newUser);
        }
        return oldUser;
    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        UserService.addLinkedAccount(oldUser, newUser);
        return newUser;
    }

    @Override
    public AuthUser update(final AuthUser knownUser) {
        UserService.setLastLoginDate(knownUser);
        return knownUser;
    }
}
