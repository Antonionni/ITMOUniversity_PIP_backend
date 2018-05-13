package services;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.*;
import data.HibernateUtils;
import enumerations.RoleType;
import io.ebean.ExpressionList;
import models.entities.LinkedAccount;
import models.entities.TokenAction;
import models.entities.UserEntity;
import models.entities.UserRolesHasUsersEntity;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.*;

public class UserDAO {
    private EntityManager entityManager = HibernateUtils.getSessionFactory().createEntityManager();

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
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "SELECT LinkedAccount.user " +
                        "from LinkedAccount la " +
                        "where la.id = :IdentityId and la.providerKey = :IdentityProvider and la.user.active = true"
                , UserEntity.class);
        query.setParameter("IdentityId", identity.getId());
        query.setParameter("IdentityProvider", identity.getProvider());
        return query;
    }

    public UserEntity findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }
        if (identity instanceof UsernamePasswordAuthUser) {
            return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
        } else {
            return getAuthUserFind(identity).getSingleResult();
        }
    }

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
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "select us from UserEntity us join us.linkedAccounts la where us.active = true and us.email = :email and la.providerKey = :providerKey", UserEntity.class);
        query.setParameter("email", identity.getEmail());
        query.setParameter("providerKey", identity.getProvider());
        return query;
    }

    public void merge(final UserEntity mainUser, final UserEntity otherUser) {
        for (final LinkedAccount acc : otherUser.getLinkedAccounts()) {
            mainUser.getLinkedAccounts().add(LinkedAccount.create(acc));
        }
        otherUser.setActive(false);
        entityManager.merge(mainUser);
        entityManager.merge(otherUser);
    }

    public UserEntity create(final AuthUser authUser, RoleType roleType) {
        final UserEntity user = new UserEntity();

        // user.permissions = new ArrayList<UserPermission>();
        // user.permissions.add(UserPermission.findByValue("printers.edit"));
        user.setActive(true);
        user.setLastLogin(new Date());
        user.setLinkedAccounts(Collections.singletonList(LinkedAccount
                .create(authUser)));

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

        entityManager.persist(user);
        UserRolesHasUsersEntity role = new UserRolesHasUsersEntity();
        role.setRoleType(roleType);
        role.setUserId(user.getId());
        role.setStartdate(new Date());
        entityManager.persist(role);
        // Ebean.saveManyToManyAssociations(user, "roles");
        // Ebean.saveManyToManyAssociations(user, "permissions");
        return user;
    }

    public void merge(final AuthUser oldUser, final AuthUser newUser) {
        merge(findByAuthUserIdentity(oldUser),
                findByAuthUserIdentity(newUser));
    }

    public Set<String> getProviders(UserEntity userEntity) {
        final Set<String> providerKeys = new HashSet<String>(
                userEntity.getLinkedAccounts().size());
        for (final LinkedAccount acc : userEntity.getLinkedAccounts()) {
            providerKeys.add(acc.getProviderKey());
        }
        return providerKeys;
    }

    public void addLinkedAccount(final AuthUser oldUser,
                                        final AuthUser newUser) {
        final UserEntity u = findByAuthUserIdentity(oldUser);
        u.getLinkedAccounts().add(LinkedAccount.create(newUser));
        entityManager.merge(u);
    }

    public void setLastLoginDate(final AuthUser knownUser) {
        final UserEntity u = findByAuthUserIdentity(knownUser);
        u.setLastLogin(new Date());
        entityManager.merge(u);
    }

    public UserEntity findByEmail(final String email) {
        try {
            return getEmailUserFind(email).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    private TypedQuery<UserEntity> getEmailUserFind(final String email) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "select us from UserEntity us where active = true and email = :email", UserEntity.class);
        query.setParameter("email", email);
        return query;
    }

    public LinkedAccount getAccountByProvider(UserEntity userEntity, final String providerKey) {
        return LinkedAccount.findByProviderKey(userEntity, providerKey);
    }

    public void verify(final UserEntity unverified) {
        // You might want to wrap this into a transaction
        unverified.setEmailValidated(true);
        entityManager.merge(unverified);
        TokenAction.deleteByUser(unverified, TokenAction.Type.EMAIL_VERIFICATION);
    }

    public void changePassword(UserEntity userEntity, final UsernamePasswordAuthUser authUser,
                               final boolean create) {
        LinkedAccount a = getAccountByProvider(userEntity, authUser.getProvider());
        if (a == null) {
            if (create) {
                a = LinkedAccount.create(authUser);
                a.setUser(userEntity);
            } else {
                throw new RuntimeException(
                        "Account not enabled for password usage");
            }
        }
        a.setProviderUserId(authUser.getHashedPassword());
        entityManager.persist(a);
    }

    public void resetPassword(UserEntity userEntity, final UsernamePasswordAuthUser authUser,
                              final boolean create) {
        // You might want to wrap this into a transaction
        this.changePassword(userEntity, authUser, create);
        TokenAction.deleteByUser(userEntity, TokenAction.Type.PASSWORD_RESET);
    }
}
