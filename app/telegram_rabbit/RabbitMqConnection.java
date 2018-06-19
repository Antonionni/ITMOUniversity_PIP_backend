package telegram_rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import config.Const;
import play.Logger;

import java.util.concurrent.TimeoutException;

public class RabbitMqConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if(connection == null) {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(Const.RABBITMQ_HOST);
            try {
                connection = connectionFactory.newConnection();
            }
            catch (Exception ex) {
                Logger.error("rabbitmq connection failed", ex);
            }
        }
        return connection;
    }
}
