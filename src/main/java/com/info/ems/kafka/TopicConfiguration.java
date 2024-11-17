package com.info.ems.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import com.info.ems.constants.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Kafka topic configuration class responsible for managing the creation and existence checks of Kafka topics.
 * <p>
 * This configuration class creates Kafka topics if they don't exist, ensuring that topics are configured with
 * specific retention and cleanup policies.
 * </p>
 */
@Configuration
@Slf4j
public class TopicConfiguration {

    private final KafkaURLConfiguration kafkaURLConfiguration;

    /**
     * Constructor to initialize Kafka URL configuration.
     * 
     * @param kafkaURLConfiguration The Kafka URL configuration bean used to get the Kafka broker URL.
     */
    @Autowired
    public TopicConfiguration(KafkaURLConfiguration kafkaURLConfiguration) {
        this.kafkaURLConfiguration = kafkaURLConfiguration;
    }

    /**
     * KafkaAdmin bean responsible for managing Kafka broker configurations.
     * 
     * @return KafkaAdmin configured with the Kafka URL.
     */
    @Bean
    public KafkaAdmin admin() {
        log.trace("Inside KafkaAdmin.");
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaURLConfiguration.getKafkaURL());
        log.trace("Kafka URL: {}", kafkaURLConfiguration.getKafkaURL());
        return new KafkaAdmin(configs);
    }

    /**
     * Creates a new Kafka topic for system notifications and OTP if it doesn't already exist.
     * <p>
     * The topic is configured with retention settings and a cleanup policy.
     * </p>
     * 
     * @return A new Kafka topic configuration.
     * @throws ExecutionException if topic creation fails.
     * @throws InterruptedException if thread is interrupted during execution.
     */
    @Bean
    public NewTopic systemNotificationOTPTopic() throws ExecutionException, InterruptedException {
        log.trace("Inside systemNotificationOTPTopic.");
        
        // Check if the topic already exists to avoid re-creating it
        if (doesTopicExist(Constants.SYSTEM_AUIDT_LOG_TOPIC)) {
            log.trace("Topic {} already exists, skipping creation.", Constants.SYSTEM_AUIDT_LOG_TOPIC);
            return null;
        }

        log.trace("Creating topic [{}] started.", Constants.SYSTEM_AUIDT_LOG_TOPIC);
        // Create a new topic with specified retention and cleanup policies
        return TopicBuilder
                .name(Constants.SYSTEM_AUIDT_LOG_TOPIC)
                .config(TopicConfig.RETENTION_MS_CONFIG, Constants.SYSTEM_AUIDT_LOG_RETENTION)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.SEGMENT_MS_CONFIG, Constants.SYSTEM_AUIDT_LOG_SEGMENT_RETENTION)
                .build();
    }

    /**
     * Checks if the specified Kafka topic already exists in the Kafka cluster.
     * 
     * @param topic The topic name to check for existence.
     * @return true if the topic exists, false otherwise.
     * @throws ExecutionException if there is an error checking topic existence.
     * @throws InterruptedException if thread is interrupted during execution.
     */
    private boolean doesTopicExist(String topic) throws ExecutionException, InterruptedException {
        log.trace("Inside doesTopicExist method for topic: {}", topic);

        // Set up the properties to connect to the Kafka broker
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaURLConfiguration.getKafkaURL());

        try (AdminClient adminClient = AdminClient.create(properties)) {
            // List all available topics
            ListTopicsResult topicsResult = adminClient.listTopics(new ListTopicsOptions().listInternal(true));
            Set<String> topicNames = topicsResult.names().get();
            log.trace("Available Topics: {}", topicNames);
            
            // Check if the topic already exists
            if (topicNames.contains(topic)) {
                log.warn("Topic already exists: {}", topic);
            }
            return topicNames.contains(topic);
        }
    }
}
