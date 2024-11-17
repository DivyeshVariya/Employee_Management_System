package com.info.ems.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.info.ems.kafka.KafkaURLConfiguration;
import com.info.ems.kafka.events.AuditLogEvent;

/**
 * Configuration class for Kafka Producer settings.
 */
@Configuration
public class KafkaProducerConfig {

    // Configuration for Kafka URLs
    private final KafkaURLConfiguration kafkaURLConfiguration;

    /**
     * Constructor for injecting Kafka URL configuration.
     * 
     * @param kafkaURLConfiguration configuration for Kafka URLs
     */
    @Autowired
    public KafkaProducerConfig(KafkaURLConfiguration kafkaURLConfiguration) {
        this.kafkaURLConfiguration = kafkaURLConfiguration;
    }

    /**
     * Bean definition for Kafka Producer handling Audit Log Events.
     * 
     * @return a configured {@link KafkaProducer} instance
     */
    @Bean(name = "auditLogEvent")
    public KafkaProducer<String, AuditLogEvent> globalSearchKafkaProducer() {
        // Kafka producer configuration properties
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaURLConfiguration.getKafkaURL());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Return a new KafkaProducer with the specified serializers
        return new KafkaProducer<>(config, new StringSerializer(), new JsonSerializer<>());
    }
}
