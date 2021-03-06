package modules;

import akka.actor.ActorRef;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.feth.play.module.mail.IMailer;
import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.MailerFactory;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.Resolver;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthProvider;
import com.feth.play.module.pa.providers.openid.OpenIdAuthProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

//import providers.MyStupidBasicAuthProvider;
//import providers.MyUsernamePasswordAuthProvider;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import jabber.JabberBot;
import jabber.XmppManager;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import play.Logger;
import play.libs.Akka;
import play.libs.Json;
import play.libs.akka.AkkaGuiceSupport;
import providers.MyUsernamePasswordAuthProvider;
import services.*;
import telegram_rabbit.*;
//import service.DataInitializer;
//import service.MyResolver;
//import service.MyUserService;

/**
 * Initial DI module.
 */
public class DependencyInjectionModule extends AbstractModule {
    static {
        ApiContextInitializer.init();
    }

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(IMailer.class, Mailer.class).build(MailerFactory.class));
        bind(ITokenActionService.class).to(TokenActionService.class);
        bind(ILinkedAccountService.class).to(LinkedAccountService.class);
        bind(IUserService.class).to(UserService.class);
        bind(ICourseListService.class).to(CourseListService.class);
        bind(ICourseService.class).to(CourseService.class);
        bind(ILessonService.class).to(LessonService.class);
        bind(IPassageService.class).to(PassageService.class);

        bind(Resolver.class).to(MyResolver.class);

        bind(PlayAuthenticate.class).to(MyPlayAuthenticate.class).asEagerSingleton();
        bind(UserIdentityService.class).asEagerSingleton();

        bind(GoogleAuthProvider.class).asEagerSingleton();
        bind(CustomExecutionContext.class);
        //bind(FacebookAuthProvider.class).asEagerSingleton();
        //bind(FoursquareAuthProvider.class).asEagerSingleton();
        bind(MyUsernamePasswordAuthProvider.class).asEagerSingleton();
        bind(OpenIdAuthProvider.class).asEagerSingleton();


        Json.mapper().registerModule(new GuavaModule());
        Json.mapper().registerModule(new Jdk8Module());
        bind(JabberBot.class);
        bind(TelegramAuthProvider.class).asEagerSingleton();
        bind(CoursachelloBot.class);
        bind(TelegramInitialization.class).asEagerSingleton();
        bind(ApplicationStart.class).asEagerSingleton();
        //bindActor(PassageActor.class, "passageaaaa-actor");
        //bind(TwitterAuthProvider.class).asEagerSingleton();
        //bind(LinkedinAuthProvider.class).asEagerSingleton();
        //bind(VkAuthProvider.class).asEagerSingleton();
        //bind(XingAuthProvider.class).asEagerSingleton();
        //bind(UntappdAuthProvider.class).asEagerSingleton();
        //bind(PocketAuthProvider.class).asEagerSingleton();
        //bind(GithubAuthProvider.class).asEagerSingleton();
        //bind(SpnegoAuthProvider.class).asEagerSingleton();
        //bind(EventBriteAuthProvider.class).asEagerSingleton();
    }
}
