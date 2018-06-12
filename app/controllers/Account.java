package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import config.RolesConst;
import models.entities.UserEntity;
import play.data.Form;
import play.data.FormFactory;
import play.data.format.Formats.NonEmpty;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import services.IUserService;
import services.UserProvider;
import views.html.account.link;

import javax.inject.Inject;

public class Account extends Controller {

	public static class Accept {

		@Required
		@NonEmpty
		public Boolean accept;

		public Boolean getAccept() {
			return accept;
		}

		public void setAccept(Boolean accept) {
			this.accept = accept;
		}

	}

	public static class PasswordChange {
		@MinLength(5)
		@Required
		public String password;

		@MinLength(5)
		@Required
		public String repeatPassword;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRepeatPassword() {
			return repeatPassword;
		}

		public void setRepeatPassword(String repeatPassword) {
			this.repeatPassword = repeatPassword;
		}

		public String validate() {
			if (password == null || !password.equals(repeatPassword)) {
				return "wrong password";
			}
			return null;
		}
	}

	private final Form<Accept> ACCEPT_FORM;
	private final Form<PasswordChange> PASSWORD_CHANGE_FORM;

	private final PlayAuthenticate auth;
	private final UserProvider userProvider;
	private final MyUsernamePasswordAuthProvider myUsrPaswProvider;
	private final IUserService UserService;

	private final MessagesApi msg;

	@Inject
	public Account(final PlayAuthenticate auth, final UserProvider userProvider,
				   final MyUsernamePasswordAuthProvider myUsrPaswProvider,
				   final FormFactory formFactory, final MessagesApi msg, final IUserService UserService) {
		this.auth = auth;
		this.userProvider = userProvider;
		this.myUsrPaswProvider = myUsrPaswProvider;

		this.ACCEPT_FORM = formFactory.form(Accept.class);
		this.PASSWORD_CHANGE_FORM = formFactory.form(PasswordChange.class);
		this.UserService = UserService;

		this.msg = msg;
	}

	@Transactional
	@SubjectPresent
	public Result link() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(link.render(this.userProvider, this.auth));
	}

	@Transactional
	@Restrict(@Group(RolesConst.AuthenticatedUser))
	public Result verifyEmail() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final UserEntity user = this.userProvider.getUser(session());
		if (user.isEmailValidated()) {
			// E-Mail has been validated already
			flash(Application.FLASH_MESSAGE_KEY,
					this.msg.preferred(request()).at("playauthenticate.verify_email.error.already_validated"));
		} else if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
			flash(Application.FLASH_MESSAGE_KEY, this.msg.preferred(request()).at(
					"playauthenticate.verify_email.message.instructions_sent",
					user.getEmail()));
			this.myUsrPaswProvider.sendVerifyEmailMailingAfterSignup(user, ctx());
		} else {
			flash(Application.FLASH_MESSAGE_KEY, this.msg.preferred(request()).at(
					"playauthenticate.verify_email.error.set_email_first",
					user.getEmail()));
		}
		return redirect(routes.Application.profile());
	}

	@Transactional
	@Restrict(@Group(RolesConst.AuthenticatedUser))
	public Result changePassword() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final UserEntity u = this.userProvider.getUser(session());

		if (!u.isEmailValidated()) {
			return ok(views.html.account.unverified.render(this.userProvider));
		} else {
			return ok(views.html.account.password_change.render(this.userProvider, PASSWORD_CHANGE_FORM));
		}
	}

	@Transactional
	@Restrict(@Group(RolesConst.AuthenticatedUser))
	public Result doChangePassword() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<PasswordChange> filledForm = PASSWORD_CHANGE_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not select whether to link or not link
			return badRequest(views.html.account.password_change.render(this.userProvider, filledForm));
		} else {
			final UserEntity user = this.userProvider.getUser(session());
			final String newPassword = filledForm.get().password;
			UserService.changePassword(user, new MyUsernamePasswordAuthUser(newPassword),
					true);
			flash(Application.FLASH_MESSAGE_KEY,
					this.msg.preferred(request()).at("playauthenticate.change_password.success"));
			return redirect(routes.Application.profile());
		}
	}

	@Transactional
	@SubjectPresent
	public Result askLink() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final AuthUser u = this.auth.getLinkUser(session());
		if (u == null) {
			// account to link could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}
		return ok(views.html.account.ask_link.render(this.userProvider, ACCEPT_FORM, u));
	}

	@Transactional
	@SubjectPresent
	public Result doLink() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final AuthUser u = this.auth.getLinkUser(session());
		if (u == null) {
			// account to link could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}

		final Form<Accept> filledForm = ACCEPT_FORM.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not select whether to link or not link
			return badRequest(views.html.account.ask_link.render(this.userProvider, filledForm, u));
		} else {
			// User made a choice :)
			final boolean link = filledForm.get().accept;
			if (link) {
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.accounts.link.success"));
			}
			return this.auth.link(ctx(), link);
		}
	}

	@Transactional
	@SubjectPresent
	public Result askMerge() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		// this is the currently logged in user
		final AuthUser aUser = this.auth.getUser(session());

		// this is the user that was selected for a login
		final AuthUser bUser = this.auth.getMergeUser(session());
		if (bUser == null) {
			// user to merge with could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}

		// You could also get the local user object here via
		// User.findByAuthUserIdentity(newUser)
		return ok(views.html.account.ask_merge.render(this.userProvider, ACCEPT_FORM, aUser, bUser));
	}

	@Transactional
	@SubjectPresent
	public Result doMerge() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		// this is the currently logged in user
		final AuthUser aUser = this.auth.getUser(session());

		// this is the user that was selected for a login
		final AuthUser bUser = this.auth.getMergeUser(session());
		if (bUser == null) {
			// user to merge with could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}

		final Form<Accept> filledForm = ACCEPT_FORM.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not select whether to merge or not merge
			return badRequest(views.html.account.ask_merge.render(this.userProvider, filledForm, aUser, bUser));
		} else {
			// User made a choice :)
			final boolean merge = filledForm.get().accept;
			if (merge) {
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.accounts.merge.success"));
			}
			return this.auth.merge(ctx(), merge);
		}
	}

}
