package org.maru.sink;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class KeyCustomSinkConfig extends AbstractConfig {
    public static final String EXCHANGE_KEY = "exchange_key";
    private static final String EXCHANGE_KEY_DEFAULT_VALUE = "indexInfo";
    private static final String EXCHANGE_KEY_DOC = "Key value to be added by extracting from the topic value";

    public static final String KAFKA_BOOTSTRAP = "bootstrap.servers";
    public static final String KAFKA_BOOTSTRAP_DEFAULT_VALUE = "localhost:9092";
    public static final String KAFKA_BOOTSTRAP_DOC = "Kafka bootstrap of topic to send";

    public static final String SEND_TOPIC_NAME = "send.topic";
    private static final String SEND_TOPIC_DEFAULT_VALUE = "elt-log-exchange";
    private static final String SEND_TOPIC_DOC = "Topic name to send data to";


    public static ConfigDef CONFIG = new ConfigDef()
            .define(EXCHANGE_KEY, ConfigDef.Type.STRING,
                    EXCHANGE_KEY_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH, EXCHANGE_KEY_DOC)
            .define(KAFKA_BOOTSTRAP, ConfigDef.Type.STRING,
                    KAFKA_BOOTSTRAP_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH, KAFKA_BOOTSTRAP_DOC)
            .define(SEND_TOPIC_NAME, ConfigDef.Type.STRING,
                    SEND_TOPIC_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH, SEND_TOPIC_DOC);

    public KeyCustomSinkConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}
