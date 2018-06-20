package modules;

import javax.inject.*;

import config.RolesConst;
import models.entities.UserEntity;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import play.Logger;
import play.db.jpa.JPAApi;
import play.inject.ApplicationLifecycle;
import play.Environment;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import services.IUserService;
import telegram_rabbit.CoursachelloBot;

import java.util.concurrent.CompletableFuture;

// This creates an `ApplicationStart` object once at start-up.
@Singleton
public class ApplicationStart {
    private final String adminEmail = "admin@domain.com";
    @Inject
    public ApplicationStart(ApplicationLifecycle lifecycle, Environment environment, IUserService userService, JPAApi jpaApi, CoursachelloBot bot) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(bot);
        }
        catch(Exception e) {
            Logger.error("heh", e);
        }
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
