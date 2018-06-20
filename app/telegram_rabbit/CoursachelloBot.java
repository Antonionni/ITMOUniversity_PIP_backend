package telegram_rabbit;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import models.TelegramIdentity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import play.api.Configuration;
import services.IPassageService;
import services.IUserService;
import services.PassageService;

public class CoursachelloBot extends TelegramLongPollingBot {

    private final IPassageService PassageService;
    private final Configuration Configuration;
    private final IUserService UserService;

    @Inject
    public CoursachelloBot(IPassageService passageService, Configuration configuration, IUserService userService) {
        super(getDefaultBotOptions(configuration));
        this.PassageService = passageService;
        this.Configuration = configuration;
        this.UserService = userService;

    }

    private static DefaultBotOptions getDefaultBotOptions(Configuration configuration) {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        String proxyHost = configuration.underlying().getString("telegrambot.proxy.host");
        if (Strings.isNullOrEmpty(proxyHost)) {
            return botOptions;
        }

        int port = Integer.parseInt(configuration.underlying().getString("telegrambot.proxy.port"));
        HttpHost httpHost = new HttpHost(proxyHost, port);
        botOptions.setHttpProxy(httpHost);

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setProxy(httpHost);

        String proxyUser = configuration.underlying().getString("telegrambot.proxy.user");

        if (!Strings.isNullOrEmpty(proxyUser)) {
            String proxyPassword = configuration.underlying().getString("telegrambot.proxy.pass");
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(proxyHost, port),
                    new UsernamePasswordCredentials(proxyUser, proxyPassword));
            botOptions.setCredentialsProvider(credsProvider);
            requestConfigBuilder.setAuthenticationEnabled(true);
        }

        botOptions.setRequestConfig(requestConfigBuilder.build());
        return botOptions;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Integer userId = update.getMessage().getFrom().getId();
            UserService.findByAuthUserIdentity(new TelegramIdentity(userId.toString()));
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return Configuration.underlying().getString("telegrambot.username");
    }

    @Override
    public String getBotToken() {
        return Configuration.underlying().getString("telegrambot.token");
    }
}
