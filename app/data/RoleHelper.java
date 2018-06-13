package data;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import enumerations.RoleType;
import models.ApiResponse;
import models.entities.UserEntity;
import play.mvc.Http;
import services.IUserService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

public class RoleHelper {
    @Inject
    private static IUserService UserService;
    @Inject
    private static PlayAuthenticate AuthService;

    public static UserEntity getCurrentUser() {
        AuthUser user = AuthService.getUser(Http.Context.current());
        if(user == null) {
            return null;
        }
        return UserService.findByAuthUserIdentity(user);
    }
    public static Collection<RoleType> getUserRoles() {
        UserEntity userEntity = getCurrentUser();
        if(userEntity == null) {
            return Collections.emptyList();
        }
        return userEntity.getRoleTypes();
    }

    public static Collection<RoleType> getUserRoles(UserEntity userEntity) {
        if(userEntity == null) {
            return Collections.emptyList();
        }
        return userEntity.getRoleTypes();
    }

    public static boolean currentUserOrAdmin(int userId) {
        UserEntity userEntity = getCurrentUser();
        if(userEntity == null) {
            return false;
        }

        boolean isCurrentUser = userEntity.getId() == userId;
        return isCurrentUser || getUserRoles(userEntity).contains(RoleType.Admin);
    }
}
