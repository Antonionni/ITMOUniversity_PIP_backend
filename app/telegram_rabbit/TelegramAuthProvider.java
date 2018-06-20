package telegram_rabbit;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AccessTokenException;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.providers.AuthProvider;
import com.feth.play.module.pa.providers.ext.ExternalAuthProvider;
import com.feth.play.module.pa.providers.oauth2.OAuth2AuthInfo;
import com.feth.play.module.pa.providers.oauth2.OAuth2AuthProvider;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import models.TelegramIdentity;
import org.h2.security.SHA256;
import play.api.Configuration;
import play.inject.ApplicationLifecycle;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import java.nio.charset.Charset;
import java.util.*;

public class TelegramAuthProvider extends ExternalAuthProvider {

    private final Configuration Configuration;

    @Inject
    public TelegramAuthProvider(PlayAuthenticate auth, ApplicationLifecycle lifecycle, Configuration configuration) {
        super(auth, lifecycle);
        this.Configuration = configuration;
    }

    @Override
    public String getKey() {
        return "telegram";
    }

    @Override
    protected List<String> neededSettingKeys() {
        return null;
    }

    @Override
    public Object authenticate(Http.Context context, Object payload) throws AuthException {
        String botToken = Configuration.underlying().getString("telegrambot.token");
        HashCode botHash = Hashing.sha256().hashString(botToken, Charset.defaultCharset());
        ArrayList<String> parameters = new ArrayList<>();
        Map<String,String[]> queryString = context.request().queryString();
        String requestHash = queryString.get("hash")[0];
        queryString.forEach((key, values) -> {
            parameters.add(key + "=" + values[0]);
        });
        parameters.sort(Comparator.naturalOrder());
        parameters.removeIf(x -> x.contains("hash"));
        String parameterString = String.join("\n", parameters);
        HashCode serverHash = Hashing.hmacSha256(botHash.asBytes()).hashString(parameterString, Charset.defaultCharset());
        if(!requestHash.equals(serverHash.toString())) {
            throw new AccessDeniedException(getKey());
        }

        String first_name = queryString.get("first_name")[0];
        String last_name = queryString.get("last_name")[0];
        return new TelegramIdentity(
                queryString.get("id")[0],
                first_name,
                last_name,
                first_name + " " + last_name,
                queryString.get("photo_url")[0]);
    }

    @Override
    public boolean isExternal() {
        return false;
    }
}
