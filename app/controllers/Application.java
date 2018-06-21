package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.feth.play.module.pa.PlayAuthenticate;
import config.RolesConst;
import models.entities.UserEntity;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import services.IUserService;
import services.UserProvider;
import views.html.*;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Application extends Controller {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";

    private final PlayAuthenticate auth;

    private final MyUsernamePasswordAuthProvider provider;

    private final UserProvider userProvider;
    private final IUserService UserService;

    public static String formatTimestamp(final long t) {
        return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
    }

    @Inject
    public Application(final PlayAuthenticate auth, final MyUsernamePasswordAuthProvider provider,
                       final UserProvider userProvider, final IUserService UserService) {
        this.auth = auth;
        this.provider = provider;
        this.userProvider = userProvider;
        this.UserService = UserService;
    }

    public Result index() {
        return redirect("/");
    }


    @Restrict(@Group(RolesConst.AuthenticatedUser))
    public Result restricted() {
        final UserEntity localUser = this.userProvider.getUser(session());
        return ok(restricted.render(this.userProvider, localUser));
    }

    @Restrict(@Group(RolesConst.AuthenticatedUser))
    public Result profile() {

        final UserEntity localUser = userProvider.getUser(session());
        return ok(profile.render(this.auth, this.userProvider, localUser, UserService));
    }

    public Result login() {
        return ok(login.render(this.auth, this.userProvider,  this.provider.getLoginForm()));
    }

    public Result doLogin() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final Form<MyUsernamePasswordAuthProvider.MyLogin> filledForm = this.provider.getLoginForm()
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill everything properly
            return badRequest(login.render(this.auth, this.userProvider, filledForm));
        } else {
            // Everything was filled
            return this.provider.handleLogin(ctx());
        }
    }

    public Result signup() {
        return ok(signup.render(this.auth, this.userProvider, this.provider.getSignupForm()));
    }

    public Result jsRoutes() {
        return ok(
                play.routing.JavaScriptReverseRouter.create("jsRoutes",
                        routes.javascript.Signup.forgotPassword()))
                .as("text/javascript");

    }

    public Result doSignup() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());

        final Form<MyUsernamePasswordAuthProvider.MySignup> filledForm = this.provider.getSignupForm().bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill everything properly
            return badRequest(signup.render(this.auth, this.userProvider, filledForm));
        } else {
            // Everything was filled
            // do something with your part of the form before handling the user
            // signup
            return this.provider.handleSignup(ctx());
        }
    }
}