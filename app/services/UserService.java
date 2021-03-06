package services;

import Exceptions.UnauthorizedAccessException;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.*;
import enumerations.RoleType;
import models.entities.*;
import models.serviceEntities.UserData.*;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import providers.MyUsernamePasswordAuthUser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserService extends BaseService implements IUserService {
    private final ILinkedAccountService LinkedAccountService;
    private final ITokenActionService TokenActionService;
    private final PlayAuthenticate AuthService;
    private final Collection<RoleType> PRIVILEGED_ROLE_TYPES = Arrays.asList(RoleType.Admin, RoleType.ApprovedTeacher);

    @Inject
    public UserService(ILinkedAccountService LinkedAccountService, ITokenActionService TokenActionService, JPAApi jpaApi, HttpExecutionContext ec, PlayAuthenticate auth) {
        super(jpaApi, ec);
        this.LinkedAccountService = LinkedAccountService;
        this.TokenActionService = TokenActionService;
        this.AuthService = auth;
    }

    @Override
    public boolean existsByAuthUserIdentity(
            final AuthUserIdentity identity) {
        return wrap(em -> {
            final TypedQuery<UserEntity> exp;
            if (identity instanceof UsernamePasswordAuthUser) {
                exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
            } else {
                exp = getAuthUserFind(identity);
            }

            return exp.setMaxResults(1).getResultList().size() > 0;
        });
    }

    private TypedQuery<UserEntity> getAuthUserFind(
            final AuthUserIdentity identity) {
        TypedQuery<UserEntity> query = JpaApi.em().createQuery(
                "SELECT la.user " +
                        "from LinkedAccount la join fetch la.user.linkedAccounts " +
                        "where la.providerUserId = :IdentityId and la.providerKey = :IdentityProvider and la.user.active = true"
                , UserEntity.class);
        query.setParameter("IdentityId", identity.getId());
        query.setParameter("IdentityProvider", identity.getProvider());
        return query;
    }

    @Override
    public UserEntity findByAuthUserIdentity(final AuthUserIdentity identity) {
        return wrap(em -> {
            if (identity == null) {
                return null;
            }
            if (identity instanceof UsernamePasswordAuthUser) {
                return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
            } else {
                try {
                    return getAuthUserFind(identity).getSingleResult();
                } catch (NoResultException ex) {
                    return null;
                }
            }
        });
    }

    @Override
    public UserEntity findByUsernamePasswordIdentity(
            final UsernamePasswordAuthUser identity) {
        return wrap(em -> {
            try {
                return getUsernamePasswordAuthUserFind(identity).getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        });
    }

    private TypedQuery<UserEntity> getUsernamePasswordAuthUserFind(
            final UsernamePasswordAuthUser identity) {
            TypedQuery<UserEntity> query = JpaApi.em().createQuery(
                    "select us from UserEntity us join fetch us.linkedAccounts la where us.active = :activeAccount and us.email = :email and la.providerKey = :providerKey", UserEntity.class);
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
                        })), ec.current());
    }

    public CompletionStage<Optional<AggregatedUser>> getTeacher(int id) {
        return supplyAsync(() -> wrap(em ->
                getUserByIDandRole(id, RoleType.Teacher)
                        .map(x -> {
                            AggregatedUser aggregatedUser = ToAggregatedUser(x);
                            return AddTeacherInfo(x, aggregatedUser);
                        })), ec.current());
    }

    public CompletionStage<Optional<AggregatedUser>> getUserAndGatherDataForRoles(int id, Collection<RoleType> roles) {
        return supplyAsync(() -> wrap(em -> getUserById(id)
                .map(x -> {
                    AggregatedUser aggregatedUser = ToAggregatedUser(x);
                    AddDataForRoles(roles, x, aggregatedUser);
                    return aggregatedUser;
                })), ec.current());
    }

    public CompletionStage<Collection<AggregatedUser>> getUsersList() {
        return supplyAsync(() -> wrap(em -> {
            TypedQuery<UserEntity> query = em.createQuery("select user from UserEntity user", UserEntity.class);
            return collectUsers(query);
        }));
    }

    public CompletionStage<Collection<AggregatedUser>> getUsersList(RoleType roleType) {
        return supplyAsync(() -> wrap(em -> {
            TypedQuery<UserEntity> query = em.createQuery("select user from UserEntity user join user.userRoles roles where roles.roleType = :userRole", UserEntity.class);
            query.setParameter("userRole", roleType);
            return collectUsers(query);
        }));
    }

    private Collection<AggregatedUser> collectUsers(TypedQuery<UserEntity> query) {
        return ConvertAggregatedUsers(query.getResultList());
    }

    public Collection<AggregatedUser> ConvertAggregatedUsers(Collection<UserEntity> userEntities) {
        return userEntities.stream().map(UserService::ToAggregsatedUser).collect(Collectors.toList());
    }

    public static AggregatedUser ToAggregsatedUser(UserEntity userEntity) {
        AggregatedUser aggregatedUser = ToAggregatedUser(userEntity);
        AddDataForRoles(Arrays.asList(RoleType.values()), userEntity, aggregatedUser);
        return aggregatedUser;
    }

    public CompletionStage<Optional<AggregatedUser>> getProfileData() {
        return supplyAsync(() -> wrap(em -> {
            UserEntity userEntity = getCurrentUser();
            if(userEntity == null) {
                return Optional.empty();
            }
            AggregatedUser aggregatedUser = ToAggregatedUser(userEntity);
            AddDataForRoles(userEntity.getRoleTypes(), userEntity, aggregatedUser);
            return Optional.of(aggregatedUser);
        }), ec.current());
    }

    private static void AddDataForRoles(Collection<RoleType> roles, UserEntity x, AggregatedUser aggregatedUser) {
        ArrayList<RoleType> roless = new ArrayList<>(roles);
        for(int i = 0; i < roles.size(); i++) {
            switch (roless.get(i)) {
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
        }
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

    public static AggregatedUser ToAggregatedUser(@NotNull UserEntity user) {
        return new AggregatedUser(new BaseUser(user));

    }

    private static AggregatedUser AddStudentInfo(@NotNull UserEntity userEntity, @NotNull AggregatedUser user) {
        user.setStudent(new Student(userEntity));
        return user;
    }

    private static AggregatedUser AddTeacherInfo(UserEntity userEntity, AggregatedUser user) {
        user.setTeacher(new Teacher(userEntity));
        return user;
    }

    @Override
    public void merge(final UserEntity mainUser, final UserEntity otherUser) {
        wrap(em -> {
            for (final LinkedAccount acc : otherUser.getLinkedAccounts()) {
                mainUser.getLinkedAccounts().add(LinkedAccountService.create(acc));
            }
            otherUser.setActive(false);
            em.merge(mainUser);
            em.merge(otherUser);
            return mainUser;
        });
    }

    public CompletionStage<Boolean> update(AggregatedUser aggregatedUser) {
        return supplyAsync(() -> wrap(em -> {
            int userId = aggregatedUser.getBaseUser().getId();
            if(!currentUserOrAdmin(userId)) {
                throw new UnauthorizedAccessException();
            }
            UserEntity userEntity = em.find(UserEntity.class, userId);
            if(userEntity == null) {
                return false;
            }
            UpdateWithBaseUserData(userEntity, aggregatedUser.getBaseUser());
            aggregatedUser.getStudent().map(x -> UpdateWithStudentData(userEntity, x));
            aggregatedUser.getTeacher().map(x -> UpdateWithTeacherData(userEntity, x));
            JpaApi.em().merge(userEntity);
            return true;
        }),ec.current());
    }

    private UserEntity UpdateWithBaseUserData(UserEntity userEntity, BaseUser user) {
        userEntity.setEmail(user.getEmail());
        userEntity.setName(user.getName());
        userEntity.setFirstname(user.getFirstName());
        userEntity.setSecondname(user.getLastName());
        userEntity.setUpdatedat(new Date());
        userEntity.setEmailValidated(user.isEmailValidated());
        userEntity.setActive(user.isActive());
        userEntity.setLastLogin(new Date());
        userEntity.setBirthDate(user.getBirthDate());
        userEntity.setPhotoUrl(user.getPhotoUrl());
        userEntity.setPlaceOfStudy(user.getPlaceOfStudy());
        updateRoles(userEntity, user.getRoles());
        return userEntity;
    }

    private UserEntity UpdateWithStudentData(UserEntity userEntity, Student studentData) {
        //userEntity.setStudentCourses(studentData.);
        return userEntity;
    }

    private UserEntity UpdateWithTeacherData(UserEntity userEntity, Teacher teacherData) {
        return userEntity;
    }
    @Override
    public UserEntity create(final AuthUser authUser) {
        UserEntity resultUser = wrap(em -> {
            final UserEntity user = new UserEntity();
            user.setActive(true);
            user.setLastLogin(new Date());
            user.setCreatedat(new Date());
            user.setEmailValidated(true);
            user.setUpdatedat(new Date());
            LinkedAccount linkedAccount = LinkedAccountService.create(authUser);
            linkedAccount.setUser(user);
            ArrayList<LinkedAccount> linkedAccounts = new ArrayList<>();
            linkedAccounts.add(linkedAccount);
            user.setLinkedAccounts(linkedAccounts);
            Collection<RoleType> roles = new ArrayList<>();
            roles.add(RoleType.AuthenticatedUser);

            if (authUser instanceof EmailIdentity) {
                final EmailIdentity identity = (EmailIdentity) authUser;
                user.setEmail(identity.getEmail());
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
            if (authUser instanceof MyUsernamePasswordAuthUser) {
                final MyUsernamePasswordAuthUser identity = (MyUsernamePasswordAuthUser) authUser;
                user.setBirthDate(identity.getBirthDate());
                user.setPlaceOfStudy(identity.getPlaceOfStudy());
                roles.addAll(identity.getRoles());
            }

            em.persist(user);
            Collection<UserRolesHasUsersEntity> roleEntities = updateRoles(user, roles);
            roleEntities.forEach(em::persist);
            em.merge(linkedAccount);
            return user;
        });
        return wrap(em -> em.find(UserEntity.class, resultUser.getId()));
    }

    /*private Collection<CourseSubscriptionEntity> updateCourses(UserEntity user, Collection<CourseInfo> courses) {
        Collection<CourseSubscriptionEntity> userCourses = user.getStudentCourses();
        Collection<UserHasCourseEntityPK> userCoursesId = userCourses.stream().map(x -> x.)
        courses.stream().filter(x -> !userCourses.contains(x.getId()))
    }*/

    private Collection<UserRolesHasUsersEntity> updateRoles(UserEntity user, Collection<RoleType> roles) {
        boolean isAdmin = getCurrentUserRoles().contains(RoleType.Admin);
        roles.removeAll(user.getRoleTypes());
        return roles
                .stream()
                .distinct()
                .filter(x -> {
                    if (!isAdmin && PRIVILEGED_ROLE_TYPES.contains(x) && Http.Context.current.get() != null) {
                        throw new UnauthorizedAccessException();
                    }
                    return true;
                })
                .map(x -> {
                    UserRolesHasUsersEntity userRoleEntity = new UserRolesHasUsersEntity();
                    userRoleEntity.setUser(user);
                    userRoleEntity.setStartdate(new Date());
                    userRoleEntity.setRoleType(x);
                    return userRoleEntity;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void merge(final AuthUser oldUser, final AuthUser newUser) {
        wrap(em -> {
            merge(findByAuthUserIdentity(oldUser),
                    findByAuthUserIdentity(newUser));
            return newUser;
        });
    }

    @Override
    public Set<String> getProviders(UserEntity userEntity) {
        return wrap(em -> {
            final Set<String> providerKeys = new HashSet<String>(
                    userEntity.getLinkedAccounts().size());
            for (final LinkedAccount acc : userEntity.getLinkedAccounts()) {
                providerKeys.add(acc.getProviderKey());
            }
            return providerKeys;
        });
    }

    @Override
    public void addLinkedAccount(final AuthUser oldUser,
                                 final AuthUser newUser) {
        wrap(em -> {
            final UserEntity u = findByAuthUserIdentity(oldUser);
            LinkedAccount newLinkAccount = LinkedAccountService.create(newUser);
            newLinkAccount.setUser(u);
            u.getLinkedAccounts().add(newLinkAccount);
            return JpaApi.em().merge(u);
        });
    }

    @Override
    public void setLastLoginDate(final AuthUser knownUser) {
        wrap(em -> {
            final UserEntity u = findByAuthUserIdentity(knownUser);
            if (u != null) {
                u.setLastLogin(new Date());
                JpaApi.em().merge(u);
            }
            return u;
        });
    }

    @Override
    public UserEntity findByEmail(final String email) {
        return wrap(em -> {
            try {
                return getEmailUserFind(email).getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        });
    }

    private TypedQuery<UserEntity> getEmailUserFind(final String email) {
        TypedQuery<UserEntity> query = JpaApi.em().createQuery(
                "select us from UserEntity us where active = true and email = :email", UserEntity.class);
        query.setParameter("email", email);
        return query;
    }

    @Override
    public LinkedAccount getAccountByProvider(UserEntity userEntity, final String providerKey) {
        return wrap(em -> LinkedAccountService.findByProviderKey(userEntity, providerKey));
    }

    @Override
    public void verify(final UserEntity unverified) {
        wrap(em -> {
            unverified.setEmailValidated(true);
            em.merge(unverified);
            TokenActionService.deleteByUser(unverified, TokenAction.Type.EMAIL_VERIFICATION);
            return unverified;
        });
    }

    @Override
    public void changePassword(UserEntity userEntity, final UsernamePasswordAuthUser authUser,
                               final boolean create) {
        wrap(em -> {
            LinkedAccount a = getAccountByProvider(userEntity, authUser.getProvider());
            if (a == null) {
                if (create) {
                    a = LinkedAccountService.create(authUser);
                    a.setUser(userEntity);
                    a.setProviderUserId(authUser.getHashedPassword());
                    em.persist(a);
                    return a;
                } else {
                    throw new RuntimeException(
                            "Account not enabled for password usage");
                }
            }
            a.setProviderUserId(authUser.getHashedPassword());
            em.merge(a);
            return a;
        });
    }

    @Override
    public void resetPassword(UserEntity userEntity, final UsernamePasswordAuthUser authUser,
                              final boolean create) {
        wrap(em -> {
            this.changePassword(userEntity, authUser, create);
            TokenActionService.deleteByUser(userEntity, TokenAction.Type.PASSWORD_RESET);
            return userEntity;
        });
    }

    public UserEntity getCurrentUser() {
        return wrap(em -> {
            if (Http.Context.current.get() == null) {
                return null;
            }
            AuthUser user = AuthService.getUser(Http.Context.current());
            if (user == null) {
                return null;
            }
            return findByAuthUserIdentity(user);
        });
    }
    public Collection<RoleType> getCurrentUserRoles() {
        return wrap(em -> {
            UserEntity userEntity = getCurrentUser();
            if (userEntity == null) {
                return Collections.emptyList();
            }
            return userEntity.getRoleTypes();
        });
    }

    private Collection<RoleType> getUserRoles(UserEntity userEntity) {
        return wrap(em -> {
            if (userEntity == null) {
                return Collections.emptyList();
            }
            return userEntity.getRoleTypes();
        });
    }

    public boolean currentUserOrAdmin(int userId) {
        return wrap(em -> {
            UserEntity userEntity = getCurrentUser();
            if (userEntity == null) {
                return false;
            }

            boolean isCurrentUser = userEntity.getId() == userId;
            return isCurrentUser || getUserRoles(userEntity).contains(RoleType.Admin);
        });
    }
}
