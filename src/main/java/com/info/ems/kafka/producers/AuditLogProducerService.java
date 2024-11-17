package com.info.ems.kafka.producers;

import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.ems.constants.Constants;
import com.info.ems.kafka.events.AuditLogEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for producing audit log events and sending them to a Kafka topic.
 * This service handles serialization and communication with the Kafka broker.
 */
@Service
@Slf4j
public class AuditLogProducerService {

    // Kafka producer instance for sending audit log events
    private final KafkaProducer<String, AuditLogEvent> auditLogKafkaProducer;

    /**
     * Constructor for injecting dependencies into the service.
     * 
     * @param auditLogKafkaProducer the KafkaProducer instance used for sending messages
     */
    @Autowired
    public AuditLogProducerService(KafkaProducer<String, AuditLogEvent> auditLogKafkaProducer) {
        this.auditLogKafkaProducer = auditLogKafkaProducer;
    }

    /**
     * Sends an audit log event to a Kafka topic.
     * 
     * @param message the audit log event to be sent
     */
    public void sendAuditLogEventToKafka(AuditLogEvent message) {
        log.trace("Inside sendAuditLogEventToKafka method");
        log.trace("AuditLogRequest : {}", message);

        // Create a Kafka producer record with the topic name and the audit log event
        ProducerRecord<String, AuditLogEvent> producerRecord =
                new ProducerRecord<>(Constants.SYSTEM_AUIDT_LOG_TOPIC, message);

        // Asynchronously send the message and get a Future to track the operation
        Future<RecordMetadata> metadataFuture = auditLogKafkaProducer.send(producerRecord);

        // Check the status of the future to log success or cancellation
        if (metadataFuture.isCancelled()) {
            log.info("AuditLogEvent cancelled to Kafka.");
        } else {
            log.info("Sending AuditLogEvent to Kafka");
        }
    }
}
