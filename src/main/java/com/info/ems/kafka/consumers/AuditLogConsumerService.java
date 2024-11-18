package com.info.ems.kafka.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.info.ems.constants.Constants;
import com.info.ems.kafka.config.AuditLogConsumerConfig;
import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.services.AuditLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer service for handling audit log events received from a Kafka topic.
 * This service processes the consumed messages and delegates the creation of audit logs to the service layer.
 */
@Component
@Slf4j
public class AuditLogConsumerService {

    // Service for handling audit log persistence
    private final AuditLogService auditLogService;

    /**
     * Constructor for injecting dependencies into the service.
     * 
     * @param auditLogService the service responsible for creating audit logs
     */
    @Autowired
    public AuditLogConsumerService(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /**
     * Kafka listener method for consuming audit log events.
     * 
     * @param auditLogEvent the audit log event consumed from the Kafka topic
     */
    @KafkaListener(
        topics = Constants.SYSTEM_AUIDT_LOG_TOPIC, 
        groupId = AuditLogConsumerConfig.GROUP_ID, 
        containerFactory = "auditLogEventConcurrentKafkaListenerContainerFactory",
        autoStartup = "true"
    )
    public void consumeForAuditLog(AuditLogEvent auditLogEvent) {
        log.info("Consuming auditLogEvent: {}", auditLogEvent);

        // Delegate the audit log creation to the service layer
        auditLogService.createAuditLog(auditLogEvent);
    }
}
