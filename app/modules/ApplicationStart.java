package modules;

import javax.inject.*;

import com.google.common.base.Strings;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import config.Const;
import config.RolesConst;
import models.entities.UserEntity;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import play.Configuration;
import play.Logger;
import play.db.jpa.JPAApi;
import play.inject.ApplicationLifecycle;
import play.Environment;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import services.IUserService;
import telegram_rabbit.CoursachelloBot;
import telegram_rabbit.RabbitMqConnection;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

// This creates an `ApplicationStart` object once at start-up.
@Singleton
public class ApplicationStart {
    private final String adminEmail = "admin@domain.com";

    @Inject
    public ApplicationStart(ApplicationLifecycle lifecycle, Environment environment, IUserService userService, JPAApi jpaApi, CoursachelloBot bot, Configuration configuration) {

        if(configuration.underlying().getBoolean("telegrambot.enabled")) {
            try {
                Connection connection = RabbitMqConnection.getConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(Const.RABBITMQ_QUEUE, false, false, false, null);
                channel.basicPublish("", Const.RABBITMQ_QUEUE, null, "registerBot".getBytes());
                channel.close();
            } catch (IOException ex) {
                Logger.error("something wrong with app start in rabbit mq kek", ex);
            }
        }

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
    }
}
