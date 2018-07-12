package telegram_rabbit;

import com.google.inject.Inject;
import com.rabbitmq.client.*;
import config.Const;
import org.telegram.telegrambots.TelegramBotsApi;
import play.Logger;

import java.io.IOException;

public class TelegramInitialization {
    private boolean initialized = false;

    @Inject
    public TelegramInitialization(CoursachelloBot bot) {
        try {
            Logger.info("rabbit consumer start");
            Connection connection = RabbitMqConnection.getConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(Const.RABBITMQ_QUEUE, false, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Logger.info("nyam nyam");
                    String message = new String(body, "UTF-8");
                    if (message.equals("registerBot")) {
                        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
                        try {
                            telegramBotsApi.registerBot(bot);
                            initialized = true;
                            channel.basicAck(envelope.getDeliveryTag(), true);
                        } catch (Exception e) {
                            Logger.error("heh", e);
                        }
                    }
                }
            };
            channel.basicConsume(Const.RABBITMQ_QUEUE, consumer);
        } catch (Exception ex) {
            Logger.error("something wrong with rabbit", ex);
        }
    }
}
