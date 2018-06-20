package jabber;

import com.google.inject.Inject;
import jdk.nashorn.internal.runtime.logging.Loggable;
import play.Logger;
import play.api.Configuration;

public class JabberBot {

    private final Configuration Configuration;

    @Inject
    public JabberBot(Configuration configuration) {
        this.Configuration = configuration;
    }

    public void sendMessage(String message, String recipientJID) {
        try {
            String username = Configuration.underlying().getString("jabber.username");
            String password = Configuration.underlying().getString("jabber.password");
            String host = Configuration.underlying().getString("jabber.host");
            int port = Integer.parseInt(Configuration.underlying().getString("jabber.port"));

            XmppManager xmppManager = new XmppManager(host, port);

            xmppManager.init();
            xmppManager.performLogin(username, password);

            xmppManager.sendMessage(message, recipientJID);
            xmppManager.destroy();
        }
        catch (Exception e) {
            Logger.error("jabbe errror", e);
        }
    }
}
