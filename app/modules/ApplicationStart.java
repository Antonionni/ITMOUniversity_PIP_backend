package modules;

import javax.inject.*;

import config.RolesConst;
import models.entities.UserEntity;
import play.db.jpa.JPAApi;
import play.inject.ApplicationLifecycle;
import play.Environment;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import services.IUserService;

import java.util.concurrent.CompletableFuture;

// This creates an `ApplicationStart` object once at start-up.
@Singleton
public class ApplicationStart {
    private final String adminEmail = "admin@domain.com";
    @Inject
    public ApplicationStart(ApplicationLifecycle lifecycle, Environment environment, IUserService userService, JPAApi jpaApi) {
        jpaApi.withTransaction(() -> {
            if (userService.findByEmail(adminEmail) != null) {
                return;
            }

            MyUsernamePasswordAuthProvider.MySignup signupForm = new MyUsernamePasswordAuthProvider.MySignup();
            signupForm.setName("ADMIN");
            signupForm.setRoles(RolesConst.Admin);
            signupForm.setEmail(adminEmail);
            signupForm.setPassword("admin");
            UserEntity adminUser = userService.create(new MyUsernamePasswordAuthUser(signupForm));
            userService.verify(adminUser);
        });
    }
}