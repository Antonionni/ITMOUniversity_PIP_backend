package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import models.entities.UserEntity;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

public class UserIdentityService extends AbstractUserService {
    private final JPAApi JpaApi;
    private IUserService UserService;

    @Inject
    public UserIdentityService(final PlayAuthenticate auth, final IUserService UserService, JPAApi jpaApi) {
        super(auth);
        this.UserService = UserService;
        this.JpaApi = jpaApi;
    }


    @Override
    public Object save(final AuthUser authUser) {
        return JpaApi.withTransaction(() -> {
            final boolean isLinked = UserService.existsByAuthUserIdentity(authUser);
            if (!isLinked) {
                return UserService.create(authUser).getId();
            } else {
                // we have this user already, so return null
                return null;
            }
        });
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity identity) {
        // For production: Caching might be a good idea here...
        // ...and dont forget to sync the cache when users get deactivated/deleted
        return JpaApi.withTransaction(() -> {
            final UserEntity u = UserService.findByAuthUserIdentity(identity);
            if (u != null) {
                return u.getId();
            } else {
                return null;
            }
        });
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        return JpaApi.withTransaction(() -> {
            if (!oldUser.equals(newUser)) {
                UserService.merge(oldUser, newUser);
            }
            return oldUser;
        });
    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        return JpaApi.withTransaction(() -> {
            UserService.addLinkedAccount(oldUser, newUser);
            return newUser;
        });
    }

    @Override
    public AuthUser update(final AuthUser knownUser) {
        return JpaApi.withTransaction(() -> {
            // User logged in again, bump last login date
            UserService.setLastLoginDate(knownUser);
            return knownUser;
        });
    }
}
