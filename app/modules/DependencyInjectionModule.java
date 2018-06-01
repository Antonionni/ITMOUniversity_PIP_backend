package modules;

import com.feth.play.module.mail.IMailer;
import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.MailerFactory;
import com.feth.play.module.pa.Resolver;
import com.feth.play.module.pa.providers.openid.OpenIdAuthProvider;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
//import providers.MyStupidBasicAuthProvider;
//import providers.MyUsernamePasswordAuthProvider;
import play.db.jpa.DefaultJPAApi;
import providers.MyStupidBasicAuthProvider;
import providers.MyUsernamePasswordAuthProvider;
import scala.collection.Seq;
import services.*;
//import service.DataInitializer;
//import service.MyResolver;
//import service.MyUserService;

/**
 * Initial DI module.
 */
public class DependencyInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(IMailer.class, Mailer.class).build(MailerFactory.class));
        bind(ILinkedAccountDAO.class).to(LinkedAccountDAO.class);
        bind(IUserDAO.class).to(UserDAO.class);
        bind(Resolver.class).to(MyResolver.class);

        bind(UserService.class);
        //bind(GoogleAuthProvider.class).asEagerSingleton();
        //bind(FacebookAuthProvider.class).asEagerSingleton();
        //bind(FoursquareAuthProvider.class).asEagerSingleton();
        bind(MyUsernamePasswordAuthProvider.class);
        bind(OpenIdAuthProvider.class);
        bind(org.hibernate.jpa.HibernatePersistenceProvider.class);
        //bind(TwitterAuthProvider.class).asEagerSingleton();
        //bind(LinkedinAuthProvider.class).asEagerSingleton();
        //bind(VkAuthProvider.class).asEagerSingleton();
        //bind(XingAuthProvider.class).asEagerSingleton();
        //bind(UntappdAuthProvider.class).asEagerSingleton();
        //bind(PocketAuthProvider.class).asEagerSingleton();
        //bind(GithubAuthProvider.class).asEagerSingleton();
        bind(MyStupidBasicAuthProvider.class);
        //bind(SpnegoAuthProvider.class).asEagerSingleton();
        //bind(EventBriteAuthProvider.class).asEagerSingleton();

    }




}
