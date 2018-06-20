package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.Resolver;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.providers.AuthProvider;
import com.feth.play.module.pa.user.AuthUser;
import enumerations.ErrorCode;
import models.ApiResponse;
import org.jetbrains.annotations.NotNull;
import play.Configuration;
import play.Logger;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

import java.util.Collection;

import static play.mvc.Results.ok;

public class MyPlayAuthenticate extends PlayAuthenticate {

    private static final String SETTING_KEY_AFTER_AUTH_FALLBACK = "afterAuthFallback";
    private static final String SETTING_KEY_AFTER_LOGOUT_FALLBACK = "afterLogoutFallback";
    private static final String SETTING_KEY_ACCOUNT_MERGE_ENABLED = "accountMergeEnabled";
    private static final String SETTING_KEY_ACCOUNT_AUTO_LINK = "accountAutoLink";
    private static final String SETTING_KEY_ACCOUNT_AUTO_MERGE = "accountAutoMerge";

    @Inject
    public MyPlayAuthenticate(Configuration config, Resolver resolver) {
        super(config, resolver);
    }

    @NotNull
    private Collection<String> getFlash(Http.Context context) {
        return context.flash().values();
    }

    private ApiResponse<Collection<String>> getErrorResult(Http.Context context) {
        return new ApiResponse<>(ErrorCode.UndefinedError, getFlash(context));
    }

    @Override
    public Result handleAuthentication(final String provider,
                                       final Http.Context context, final Object payload) {
        final AuthProvider ap = getProvider(provider);
        if (ap == null) {
            // Provider wasn't found and/or user was fooling with our stuff -
            // tell him off:
            return Controller.notFound(Json.toJson(Messages.get(
                    "playauthenticate.core.exception.provider_not_found",
                    provider)));
        }
        try {
            final Object o = ap.authenticate(context, payload);
            if (o instanceof String) {
                if(((String) o).startsWith("/")) {
                    return Controller.forbidden(Json.toJson(getErrorResult(context)));
                }
                else {
                    return Controller.redirect((String) o);
                }
            } else if (o instanceof Result) {
                String resultText = Json.toJson(o).asText();
                Logger.debug("wow auth return result " + context.request().uri());
                return Controller.ok(Json.toJson(new ApiResponse<>(getFlash(context))));
            } else if (o instanceof AuthUser) {

                final AuthUser newUser = (AuthUser) o;
                final Http.Session session = context.session();

                // We might want to do merging here:
                // Adapted from:
                // http://stackoverflow.com/questions/6666267/architecture-for-merging-multiple-user-accounts-together
                // 1. The account is linked to a local account and no session
                // cookie is present --> Login
                // 2. The account is linked to a local account and a session
                // cookie is present --> Merge
                // 3. The account is not linked to a local account and no
                // session cookie is present --> Signup
                // 4. The account is not linked to a local account and a session
                // cookie is present --> Linking Additional account

                // get the user with which we are logged in - is null if we
                // are
                // not logged in (does NOT check expiration)

                AuthUser oldUser = getUser(session);

                // checks if the user is logged in (also checks the expiration!)
                boolean isLoggedIn = isLoggedIn(session);

                Object oldIdentity = null;

                // check if local user still exists - it might have been
                // deactivated/deleted,
                // so this is a signup, not a link
                if (isLoggedIn) {
                    oldIdentity = getUserService().getLocalIdentity(oldUser);
                    isLoggedIn = oldIdentity != null;
                    if (!isLoggedIn) {
                        // if isLoggedIn is false here, then the local user has
                        // been deleted/deactivated
                        // so kill the session
                        logout(session);
                        oldUser = null;
                    }
                }

                final Object loginIdentity = getUserService().getLocalIdentity(
                        newUser);
                final boolean isLinked = loginIdentity != null;

                final AuthUser loginUser;
                if (isLinked && !isLoggedIn) {
                    // 1. -> Login
                    loginUser = newUser;

                } else if (isLinked) {
                    // 2. -> Merge

                    // merge the two identities and return the AuthUser we want
                    // to use for the log in
                    if (isAccountMergeEnabled()
                            && !loginIdentity.equals(oldIdentity)) {
                        // account merge is enabled
                        // and
                        // The currently logged in user and the one to log in
                        // are not the same, so shall we merge?

                        if (isAccountAutoMerge()) {
                            // Account auto merging is enabled
                            loginUser = getUserService()
                                    .merge(newUser, oldUser);
                        } else {
                            // Account auto merging is disabled - forward user
                            // to merge request page
                            final Call c = getResolver().askMerge();
                            if (c == null) {
                                throw new RuntimeException(
                                        Messages.get(
                                                "playauthenticate.core.exception.merge.controller_undefined",
                                                SETTING_KEY_ACCOUNT_AUTO_MERGE));
                            }
                            storeMergeUser(newUser, session);
                            return Controller.ok(Json.toJson(new ApiResponse<>(getFlash(context))));
                        }
                    } else {
                        // the currently logged in user and the new login belong
                        // to the same local user,
                        // or Account merge is disabled, so just change the log
                        // in to the new user
                        loginUser = newUser;
                    }

                } else if (!isLoggedIn) {
                    // 3. -> Signup
                    loginUser = signupUser(newUser, session, ap);
                } else {
                    // !isLinked && isLoggedIn:

                    // 4. -> Link additional
                    if (isAccountAutoLink()) {
                        // Account auto linking is enabled

                        loginUser = getUserService().link(oldUser, newUser);
                    } else {
                        // Account auto linking is disabled - forward user to
                        // link suggestion page
                        final Call c = getResolver().askLink();
                        if (c == null) {
                            throw new RuntimeException(
                                    Messages.get(
                                            "playauthenticate.core.exception.link.controller_undefined",
                                            SETTING_KEY_ACCOUNT_AUTO_LINK));
                        }
                        storeLinkUser(newUser, session);
                        return Controller.ok(Json.toJson(new ApiResponse<>(context)));
                    }

                }

                return loginAndRedirect(context, loginUser);
            } else {
                return Controller.internalServerError(Messages
                        .get("playauthenticate.core.exception.general"));
            }
        } catch (final AuthException e) {
                final String message = e.getMessage();
                if (message != null) {
                    return Controller.internalServerError(Json.toJson(new ApiResponse<String>(message)));
                } else {
                    return Controller.internalServerError(Json.toJson(new ApiResponse<>(getFlash(context))));
                }
        }
    }

    private AuthUser signupUser(final AuthUser u, final Http.Session session, final AuthProvider provider) throws AuthException {
        final Object id = getUserService().save(u);
        if (id == null) {
            throw new AuthException(
                    Messages.get("playauthenticate.core.exception.signupuser_failed"));
        }
        provider.afterSave(u, id, session);
        return u;
    }
}
