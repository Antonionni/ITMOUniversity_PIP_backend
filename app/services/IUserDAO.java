package services;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import enumerations.RoleType;
import models.entities.LinkedAccount;
import models.entities.UserEntity;
import models.serviceEntities.UserData.AggregatedUser;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

public interface IUserDAO {
    boolean existsByAuthUserIdentity(
            AuthUserIdentity identity);

    UserEntity findByAuthUserIdentity(AuthUserIdentity identity);

    UserEntity findByUsernamePasswordIdentity(
            UsernamePasswordAuthUser identity);

    void merge(UserEntity mainUser, UserEntity otherUser);

    CompletionStage<Boolean> update(AggregatedUser aggregatedUser);

    UserEntity create(AuthUser authUser);

    void merge(AuthUser oldUser, AuthUser newUser);

    Set<String> getProviders(UserEntity userEntity);

    void addLinkedAccount(AuthUser oldUser,
                          AuthUser newUser);

    void setLastLoginDate(AuthUser knownUser);

    UserEntity findByEmail(String email);

    LinkedAccount getAccountByProvider(UserEntity userEntity, String providerKey);

    void verify(UserEntity unverified);

    void changePassword(UserEntity userEntity, UsernamePasswordAuthUser authUser,
                        boolean create);

    void resetPassword(UserEntity userEntity, UsernamePasswordAuthUser authUser,
                       boolean create);

    CompletionStage<Optional<AggregatedUser>> getStudent(int id);

    CompletionStage<Optional<AggregatedUser>> getTeacher(int id);

    CompletionStage<Optional<AggregatedUser>> getUserAndGatherDataForRoles(int id, Collection<RoleType> roles);
}
