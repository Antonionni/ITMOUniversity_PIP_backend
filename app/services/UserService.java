package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import models.entities.UserEntity;

import javax.inject.Inject;

public class UserService extends AbstractUserService {
    private IUserDAO IUserDao;

    @Inject
    public UserService(final PlayAuthenticate auth, final IUserDAO IUserDao) {
        super(auth);
        this.IUserDao = IUserDao;
    }

    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = IUserDao.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return IUserDao.create(authUser).getId();
        } else {
            // we have this user already, so return null
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity identity) {
        // For production: Caching might be a good idea here...
        // ...and dont forget to sync the cache when users get deactivated/deleted
        final UserEntity u = IUserDao.findByAuthUserIdentity(identity);
        if(u != null) {
            return u.getId();
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            IUserDao.merge(oldUser, newUser);
        }
        return oldUser;
    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        IUserDao.addLinkedAccount(oldUser, newUser);
        return newUser;
    }

    @Override
    public AuthUser update(final AuthUser knownUser) {
        // User logged in again, bump last login date
        IUserDao.setLastLoginDate(knownUser);
        return knownUser;
    }
}
