package controllers;

import models.entities.TokenAction;
import models.entities.UserEntity;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyLoginUsernamePasswordAuthUser;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyIdentity;
import providers.MyUsernamePasswordAuthUser;
import services.ITokenActionService;
import services.IUserService;
import services.UserProvider;

import com.feth.play.module.pa.PlayAuthenticate;
import views.html.account.signup.exists;

import javax.inject.Inject;

public class Signup extends Controller {

	private final ITokenActionService TokenActionService;

	public static class PasswordReset extends controllers.Account.PasswordChange {

		public PasswordReset() {
		}

		public PasswordReset(final String token) {
			this.token = token;
		}

		public String token;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}

	private final Form<PasswordReset> PASSWORD_RESET_FORM;

	private final Form<MyIdentity> FORGOT_PASSWORD_FORM;

	private final PlayAuthenticate auth;

	private final UserProvider userProvider;

	private final MyUsernamePasswordAuthProvider userPaswAuthProvider;

	private final MessagesApi msg;

	private final IUserService UserService;

	@Inject
	public Signup(final PlayAuthenticate auth, final UserProvider userProvider,
                  final MyUsernamePasswordAuthProvider userPaswAuthProvider,
                  final FormFactory formFactory, final MessagesApi msg, final IUserService UserService, final ITokenActionService TokenActionService) {
		this.auth = auth;
		this.userProvider = userProvider;
		this.userPaswAuthProvider = userPaswAuthProvider;
		this.PASSWORD_RESET_FORM = formFactory.form(PasswordReset.class);
		this.FORGOT_PASSWORD_FORM = formFactory.form(MyIdentity.class);
		this.UserService = UserService;
		this.TokenActionService = TokenActionService;

		this.msg = msg;
	}

	@Transactional
	public Result unverified() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(views.html.account.signup.unverified.render(this.userProvider));
	}

	@Transactional
	public Result forgotPassword(final String email) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<MyIdentity> form = FORGOT_PASSWORD_FORM;
		if (email != null && !email.trim().isEmpty()) {
			form = FORGOT_PASSWORD_FORM.fill(new MyIdentity(email));
		}
		return ok(views.html.account.signup.password_forgot.render(this.userProvider, form));
	}

	@Transactional
	public Result doForgotPassword() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<MyIdentity> filledForm = FORGOT_PASSWORD_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill in his/her email
			return badRequest(views.html.account.signup.password_forgot.render(this.userProvider, filledForm));
		} else {
			// The email address given *BY AN UNKNWON PERSON* to the form - we
			// should find out if we actually have a user with this email
			// address and whether password login is enabled for him/her. Also
			// only send if the email address of the user has been verified.
			final String email = filledForm.get().email;

			// We don't want to expose whether a given email address is signed
			// up, so just say an email has been sent, even though it might not
			// be true - that's protecting our user privacy.
			flash(Application.FLASH_MESSAGE_KEY,
					this.msg.preferred(request()).at(
							"playauthenticate.reset_password.message.instructions_sent",
							email));

			final UserEntity user = UserService.findByEmail(email);
			if (user != null) {
				// yep, we have a user with this email that is active - we do
				// not know if the user owning that account has requested this
				// reset, though.
				final MyUsernamePasswordAuthProvider provider = this.userPaswAuthProvider;
				// User exists
				if (user.isEmailValidated()) {
					provider.sendPasswordResetMailing(user, ctx());
					// In case you actually want to let (the unknown person)
					// know whether a user was found/an email was sent, use,
					// change the flash message
				} else {
					// We need to change the message here, otherwise the user
					// does not understand whats going on - we should not verify
					// with the password reset, as a "bad" user could then sign
					// up with a fake email via OAuth and get it verified by an
					// a unsuspecting user that clicks the link.
					flash(Application.FLASH_MESSAGE_KEY,
							this.msg.preferred(request()).at("playauthenticate.reset_password.message.email_not_verified"));

					// You might want to re-send the verification email here...
					provider.sendVerifyEmailMailingAfterSignup(user, ctx());
				}
			}

			return redirect(routes.Application.index());
		}
	}

	/**
	 * Returns a token object if valid, null if not
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	private TokenAction tokenIsValid(final String token, final TokenAction.Type type) {
		TokenAction ret = null;
		if (token != null && !token.trim().isEmpty()) {
			final TokenAction ta = TokenActionService.findByToken(token, type);
			if (ta != null && ta.isValid()) {
				ret = ta;
			}
		}

		return ret;
	}

	@Transactional
	public Result resetPassword(final String token) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final TokenAction ta = tokenIsValid(token, TokenAction.Type.PASSWORD_RESET);
		if (ta == null) {
			return badRequest(views.html.account.signup.no_token_or_invalid.render(this.userProvider));
		}

		return ok(views.html.account.signup.password_reset.render(this.userProvider,
				PASSWORD_RESET_FORM.fill(new PasswordReset(token)))
		);
	}

	@Transactional
	public Result doResetPassword() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<PasswordReset> filledForm = PASSWORD_RESET_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.account.signup.password_reset.render(this.userProvider, filledForm));
		} else {
			final String token = filledForm.get().token;
			final String newPassword = filledForm.get().password;

			final TokenAction ta = tokenIsValid(token, TokenAction.Type.PASSWORD_RESET);
			if (ta == null) {
				return badRequest(views.html.account.signup.no_token_or_invalid.render(this.userProvider));
			}
			final UserEntity u = ta.targetUser;
			try {
				// Pass true for the second parameter if you want to
				// automatically create a password and the exception never to
				// happen
				UserService.resetPassword(u, new MyUsernamePasswordAuthUser(newPassword),
						false);
			} catch (final RuntimeException re) {
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.reset_password.message.no_password_account"));
			}
			final boolean login = this.userPaswAuthProvider.isLoginAfterPasswordReset();
			if (login) {
				// automatically log in
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.reset_password.message.success.auto_login"));

				return this.auth.loginAndRedirect(ctx(),
						new MyLoginUsernamePasswordAuthUser(u.getEmail()));
			} else {
				// send the user to the login page
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.reset_password.message.success.manual_login"));
			}
			return redirect(routes.Application.login());
		}
	}

	@Transactional
	public Result oAuthDenied(final String getProviderKey) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(views.html.account.signup.oAuthDenied.render(this.userProvider, getProviderKey));
	}

	@Transactional
	public Result exists() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(exists.render(this.userProvider));
	}

	@Transactional
	public Result verify(final String token) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final TokenAction ta = tokenIsValid(token, TokenAction.Type.EMAIL_VERIFICATION);
		if (ta == null) {
			return badRequest(views.html.account.signup.no_token_or_invalid.render(this.userProvider));
		}
		final String email = ta.targetUser.getEmail();
		UserService.verify(ta.targetUser);
		flash(Application.FLASH_MESSAGE_KEY,
				this.msg.preferred(request()).at("playauthenticate.verify_email.success", email));
		if (this.userProvider.getUser(session()) != null) {
			return redirect(routes.Application.index());
		} else {
			return redirect(routes.Application.login());
		}
	}
}
