package services;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.*;
import enumerations.RoleType;
import models.entities.*;
import models.serviceEntities.UserData.*;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserDAO extends BaseService implements IUserDAO {

    private final ILinkedAccountDAO ILinkedAccountDAO;
    private final ITokenActionDAO ITokenActionDAO;

    @Inject
    public UserDAO(ILinkedAccountDAO ILinkedAccountDAO, ITokenActionDAO ITokenActionDAO, JPAApi jpaApi, CustomExecutionContext ec) {
        super(jpaApi, ec);
        this.ILinkedAccountDAO = ILinkedAccountDAO;
        this.ITokenActionDAO = ITokenActionDAO;
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

    public CompletionStage<Optional<AggregatedUser>> getStudent(int id) {
        return supplyAsync(() -> wrap(em ->
                getUserByIDandRole(id, RoleType.Student)
                        .map(x -> {
                            AggregatedUser aggregatedUser = ToAggregatedUser(x);
                            return AddStudentInfo(x, aggregatedUser);
                        })), ec);
    }

    public CompletionStage<Optional<AggregatedUser>> getTeacher(int id) {
        return supplyAsync(() -> wrap(em ->
                getUserByIDandRole(id, RoleType.Teacher)
                        .map(x -> {
                            AggregatedUser aggregatedUser = ToAggregatedUser(x);
                            return AddTeacherInfo(x, aggregatedUser);
                        })), ec);
    }

    public CompletionStage<Optional<AggregatedUser>> getUserAndGatherDataForRoles(int id, Collection<RoleType> roles) {
        return supplyAsync(() -> wrap(em -> getUserById(id)
                .map(x -> {
                    AggregatedUser aggregatedUser = ToAggregatedUser(x);
                    roles.forEach(role -> {
                        switch (role) {
                            case Student:
                                AddStudentInfo(x, aggregatedUser);
                            case Teacher:
                                AddTeacherInfo(x, aggregatedUser);
                                break;
                            case Admin:
                                break;
                            case AuthenticatedUser:
                                break;
                        }
                    });
                    return aggregatedUser;
                })), ec);
    }

    private Optional<UserEntity> getUserById(int id) {
        EntityManager em = JpaApi.em();
        UserEntity user = em.find(UserEntity.class, id);
        return Optional.ofNullable(user);
    }

    private Optional<UserEntity> getUserByIDandRole(int id, final RoleType role) {
        Optional<UserEntity> user = getUserById(id);
        return user.filter(x -> x.getRoleTypes().contains(role));
    }

    private AggregatedUser ToAggregatedUser(@NotNull UserEntity user) {
        return new AggregatedUser(new BaseUser(user));

    }

    private AggregatedUser AddStudentInfo(@NotNull UserEntity userEntity, @NotNull AggregatedUser user) {
        user.setStudent(new Student(userEntity));
        return user;
    }

    private AggregatedUser AddTeacherInfo(UserEntity userEntity, AggregatedUser user) {
        user.setTeacher(new Teacher(userEntity));
        return user;
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
        role.setUser(user);
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
