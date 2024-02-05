package org.maru.sink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class KeyCustomSinkTask extends SinkTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper objectMapper;
    private KeyCustomSinkConfig config;
    private Producer<String, Object> producer;
    private String topicName;
    private String exchangeKey;


    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        this.config = new KeyCustomSinkConfig(props);
        this.topicName = this.config.getString(KeyCustomSinkConfig.SEND_TOPIC_NAME);
        this.exchangeKey = this.config.getString(KeyCustomSinkConfig.EXCHANGE_KEY);

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.config.getString(KeyCustomSinkConfig.KAFKA_BOOTSTRAP));
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        this.producer = new KafkaProducer<>(kafkaProps);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        //topic에서 polling 되어 처리가 되어야 하는 다수의 레코드
        for (SinkRecord record: records) {
            logger.info("record is... ========>" + record.getClass().getName());
            String recordStr = (String) record.value();
            logger.info("recordStr is =====> " + recordStr);

            Map<?, ?> recordMap;
            try {
                recordMap = objectMapper.readValue(recordStr, Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            logger.info("recordMap is... ========>" + recordMap);

            Map<String, Object> payload = (Map<String, Object>) recordMap.get("payload");
            String extractKey = (String) payload.get(exchangeKey);
            sendRecord(extractKey, recordMap);

        }
    }

    private void sendRecord(String key, Object value) {
        JsonNode jsonMap = objectMapper.valueToTree(value);
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topicName, key, jsonMap);
        this.producer.send(producerRecord);
    }

    @Override
    public void stop() {

    }
}
