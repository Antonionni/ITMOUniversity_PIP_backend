package telegram_rabbit;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.Random;

public class SacredDeerCommand extends BotCommand {

    public SacredDeerCommand() {
        super("ask", "Сакральный олень делает магию");
    }

    public String[] DeerAnswers = {
            "Да",
            "Нет",
            "Это не важно",
            "Спок, бро",
            "Да, хотя зря",
            "Никогда", "100%",
            "1 из 100",
            "Не знаю. Еще разок попробуй"};

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        Random random = new Random();
        answer.setText(DeerAnswers[random.nextInt(DeerAnswers.length)]);

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error("SacredDeer", e);
        }
    }
}
