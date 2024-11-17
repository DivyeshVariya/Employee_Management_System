package com.info.ems.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * Configuration class for Kafka URL settings.
 * <p>
 * This class is responsible for retrieving the Kafka broker URL from application
 * properties and providing it to other components.
 * </p>
 */
@Component
@Getter
public class KafkaURLConfiguration {

    /**
     * Kafka broker URL, retrieved from the application properties.
     * Example property: `kafka.url=<broker-url>`
     */
    @Value("${kafka.url}")
    private String kafkaURL;

    /**
     * Retrieves the Kafka broker URL.
     *
     * @return Kafka broker URL as a string
     */
    public String getKafkaURL() {
        return kafkaURL;
    }
}
