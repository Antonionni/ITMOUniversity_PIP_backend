package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import enumerations.RoleType;
import models.entities.UserEntity;

import javax.inject.Inject;

public class UserService extends AbstractUserService {
    private UserDAO userDao;

    @Inject
    public UserService(final PlayAuthenticate auth, final UserDAO userDao) {
        super(auth);
        this.userDao = userDao;
    }

    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = userDao.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return userDao.create(authUser).getId();
        } else {
            // we have this user already, so return null
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity identity) {
        // For production: Caching might be a good idea here...
        // ...and dont forget to sync the cache when users get deactivated/deleted
        final UserEntity u = userDao.findByAuthUserIdentity(identity);
        if(u != null) {
            return u.getId();
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            userDao.merge(oldUser, newUser);
        }
        return oldUser;
    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        userDao.addLinkedAccount(oldUser, newUser);
        return newUser;
    }

    @Override
    public AuthUser update(final AuthUser knownUser) {
        // User logged in again, bump last login date
        userDao.setLastLoginDate(knownUser);
        return knownUser;
    }
}
