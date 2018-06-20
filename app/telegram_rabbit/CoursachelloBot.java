package telegram_rabbit;

import com.google.inject.Inject;
import models.TelegramIdentity;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
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
        this.PassageService = passageService;
        this.Configuration = configuration;
        this.UserService = userService;

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
