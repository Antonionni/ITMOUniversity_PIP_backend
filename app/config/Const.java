package config;

import com.typesafe.config.ConfigFactory;

public class Const {
    public static final String RABBITMQ_HOST = ConfigFactory.load().getString("rabbitmq.host");
    public static final String RABBITMQ_QUEUE = ConfigFactory.load().getString("rabbitmq.queue");
    public static final String RABBITMQ_EXCHANGE = ConfigFactory.load().getString("rabbitmq.exchange");
}
