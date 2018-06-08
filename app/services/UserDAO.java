package services;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.*;
import enumerations.RoleType;
import models.entities.LinkedAccount;
import models.entities.TokenAction;
import models.entities.UserEntity;
import models.entities.UserRolesHasUsersEntity;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.*;

public class UserDAO implements IUserDAO {

    private final ILinkedAccountDAO ILinkedAccountDAO;
    private final ITokenActionDAO ITokenActionDAO;
    private final JPAApi JpaApi;

    @Inject
    public UserDAO(ILinkedAccountDAO ILinkedAccountDAO, ITokenActionDAO ITokenActionDAO, JPAApi jpaApi) {
        this.ILinkedAccountDAO = ILinkedAccountDAO;
        this.ITokenActionDAO = ITokenActionDAO;
        this.JpaApi = jpaApi;
    }

    @Override
    public boolean existsByAuthUserIdentity(
            final AuthUserIdentity identity) {
        final TypedQuery<UserEntity> exp;
        if (identity instanceof UsernamePasswordAuthUser) {
            exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
        } else {
            exp = getAuthUserFind(identity);
        }

        return exp.setMaxResults(1).getResultList().size() > 0;
    }

    private TypedQuery<UserEntity> getAuthUserFind(
            final AuthUserIdentity identity) {
        TypedQuery<UserEntity> query = JpaApi.em().createQuery(
                "SELECT la.user " +
                        "from LinkedAccount la " +
                        "where la.providerUserId = :IdentityId and la.providerKey = :IdentityProvider and la.user.active = true"
                , UserEntity.class);
        query.setParameter("IdentityId", identity.getId());
        query.setParameter("IdentityProvider", identity.getProvider());
        return query;
    }

    @Override
    public UserEntity findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }
        if (identity instanceof UsernamePasswordAuthUser) {
            return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
        } else {
            try {
                return getAuthUserFind(identity).getSingleResult();
            }
            catch (NoResultException ex) {
                return null;
            }
        }
    }

    @Override
    public UserEntity findByUsernamePasswordIdentity(
            final UsernamePasswordAuthUser identity) {
        try {
            return getUsernamePasswordAuthUserFind(identity).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    private TypedQuery<UserEntity> getUsernamePasswordAuthUserFind(
            final UsernamePasswordAuthUser identity) {
        EntityManager em = JpaApi.em();
        TypedQuery<UserEntity> query = JpaApi.em().createQuery(
                "select us from UserEntity us join us.linkedAccounts la where us.active = :activeAccount and us.email = :email and la.providerKey = :providerKey", UserEntity.class);
        query.setParameter("activeAccount", Boolean.TRUE);
        query.setParameter("email", identity.getEmail());
        query.setParameter("providerKey", identity.getProvider());
        return query;
    }

    @Override
    public void merge(final UserEntity mainUser, final UserEntity otherUser) {
        for (final LinkedAccount acc : otherUser.getLinkedAccounts()) {
            mainUser.getLinkedAccounts().add(ILinkedAccountDAO.create(acc));
        }
        otherUser.setActive(false);
        EntityManager em = JpaApi.em();
        em.merge(mainUser);
        em.merge(otherUser);
    }

    @Override
    public UserEntity create(final AuthUser authUser) {
        final UserEntity user = new UserEntity();

        // user.permissions = new ArrayList<UserPermission>();
        // user.permissions.add(UserPermission.findByValue("printers.edit"));
        user.setActive(true);
        user.setLastLogin(new Date());
        LinkedAccount linkedAccount = ILinkedAccountDAO.create(authUser);
        user.setLinkedAccounts(Collections.singletonList(linkedAccount));

        if (authUser instanceof EmailIdentity) {
            final EmailIdentity identity = (EmailIdentity) authUser;
            // Remember, even when getting them from FB & Co., emails should be
            // verified within the application as a security breach there might
            // break your security as well!
            user.setEmail(identity.getEmail());
            user.setEmailValidated(false);
        }

        if (authUser instanceof NameIdentity) {
            final NameIdentity identity = (NameIdentity) authUser;
            final String name = identity.getName();
            if (name != null) {
                user.setName(name);
            }
        }

        if (authUser instanceof FirstLastNameIdentity) {
            final FirstLastNameIdentity identity = (FirstLastNameIdentity) authUser;
            final String firstName = identity.getFirstName();
            final String lastName = identity.getLastName();
            if (firstName != null) {
                user.setFirstname(firstName);
            }
            if (lastName != null) {
                user.setSecondname(lastName);
            }
        }
        EntityManager em = JpaApi.em();
        em.persist(user);
        linkedAccount.setUser(user);
        em.merge(linkedAccount);
        UserRolesHasUsersEntity role = new UserRolesHasUsersEntity();
        // TODO[ASh]: wtf.
        role.setRoleType(RoleType.AuthenticatedUser);
        role.setUserId(user.getId());
        role.setStartdate(new Date());
        em.persist(role);
        return user;
    }

    @Override
    public void merge(final AuthUser oldUser, final AuthUser newUser) {
        merge(findByAuthUserIdentity(oldUser),
                findByAuthUserIdentity(newUser));
    }

    @Override
    public Set<String> getProviders(UserEntity userEntity) {
        final Set<String> providerKeys = new HashSet<String>(
                userEntity.getLinkedAccounts().size());
        for (final LinkedAccount acc : userEntity.getLinkedAccounts()) {
            providerKeys.add(acc.getProviderKey());
        }
        return providerKeys;
    }

    @Override
    public void addLinkedAccount(final AuthUser oldUser,
                                 final AuthUser newUser) {
        final UserEntity u = findByAuthUserIdentity(oldUser);
        LinkedAccount newLinkAccount = ILinkedAccountDAO.create(newUser);
        newLinkAccount.setUser(u);
        u.getLinkedAccounts().add(newLinkAccount);
        JpaApi.em().merge(u);
    }

    @Override
    public void setLastLoginDate(final AuthUser knownUser) {
        final UserEntity u = findByAuthUserIdentity(knownUser);
        if(u != null) {
            u.setLastLogin(new Date());
            JpaApi.em().merge(u);
        }
    }

    @Override
    public UserEntity findByEmail(final String email) {
        try {
            return getEmailUserFind(email).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    private TypedQuery<UserEntity> getEmailUserFind(final String email) {
        TypedQuery<UserEntity> query = JpaApi.em().createQuery(
                "select us from UserEntity us where active = true and email = :email", UserEntity.class);
        query.setParameter("email", email);
        return query;
    }

    @Override
    public LinkedAccount getAccountByProvider(UserEntity userEntity, final String providerKey) {
        return ILinkedAccountDAO.findByProviderKey(userEntity, providerKey);
    }

    @Override
    public void verify(final UserEntity unverified) {
        // You might want to wrap this into a transaction
        unverified.setEmailValidated(true);
        JpaApi.em().merge(unverified);
        ITokenActionDAO.deleteByUser(unverified, TokenAction.Type.EMAIL_VERIFICATION);
    }

    @Override
    public void changePassword(UserEntity userEntity, final UsernamePasswordAuthUser authUser,
                               final boolean create) {
        LinkedAccount a = getAccountByProvider(userEntity, authUser.getProvider());
        if (a == null) {
            if (create) {
                a = ILinkedAccountDAO.create(authUser);
                a.setUser(userEntity);
            } else {
                throw new RuntimeException(
                        "Account not enabled for password usage");
            }
        }
        a.setProviderUserId(authUser.getHashedPassword());
        JpaApi.em().persist(a);
    }

    @Override
    public void resetPassword(UserEntity userEntity, final UsernamePasswordAuthUser authUser,
                              final boolean create) {
        // You might want to wrap this into a transaction
        this.changePassword(userEntity, authUser, create);
        ITokenActionDAO.deleteByUser(userEntity, TokenAction.Type.PASSWORD_RESET);
    }
}
