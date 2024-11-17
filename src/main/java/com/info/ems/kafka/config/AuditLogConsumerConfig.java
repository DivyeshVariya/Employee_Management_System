package com.info.ems.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.info.ems.kafka.KafkaURLConfiguration;
import com.info.ems.kafka.events.AuditLogEvent;

/**
 * Configuration class for Kafka consumer settings related to the Audit Log Event.
 */
@Configuration
public class AuditLogConsumerConfig {

    // Group ID for audit log consumers
    public static final String GROUP_ID = "GROUP_FOR_AUDIT_LOG";

    // Configuration for Kafka URLs
    private final KafkaURLConfiguration kafkaURLConfiguration;

    /**
     * Constructor for injecting Kafka URL configuration.
     * 
     * @param kafkaURLConfiguration configuration for Kafka URLs
     */
    @Autowired
    public AuditLogConsumerConfig(KafkaURLConfiguration kafkaURLConfiguration) {
        this.kafkaURLConfiguration = kafkaURLConfiguration;
    }

    /**
     * Creates a {@link ConsumerFactory} for Audit Log Events.
     * 
     * @return the consumer factory configured for Audit Log Events
     */
    @Bean
    public ConsumerFactory<String, AuditLogEvent> auditLogEventConsumerFactory() {
        JsonDeserializer<AuditLogEvent> deserializer =
                new JsonDeserializer<>(AuditLogEvent.class, false);

        // Configure the deserializer
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        // Kafka consumer properties
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaURLConfiguration.getKafkaURL());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    /**
     * Creates a {@link ConcurrentKafkaListenerContainerFactory} for listening to Audit Log Events.
     * 
     * @return the container factory for Audit Log Event listeners
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditLogEvent> auditLogEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AuditLogEvent> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // Set the consumer factory
        containerFactory.setConsumerFactory(auditLogEventConsumerFactory());

        // Optional: Uncomment the line below to set the concurrency level for the listener
        // containerFactory.setConcurrency(10);

        return containerFactory;
    }
}
