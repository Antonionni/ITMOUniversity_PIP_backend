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

public class RoleHelperService {
    @Inject
    private IUserService UserService;
    @Inject
    private PlayAuthenticate AuthService;


}
