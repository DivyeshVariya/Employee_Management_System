package com.info.ems.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.mapper.AuditLogMapper;
import com.info.ems.models.AuditLog;
import com.info.ems.repositories.AuditLogRepository;
import com.info.ems.services.AuditLogService;

/**
 * Service implementation for managing audit logs. This service provides functionality
 * to create and persist audit log entries in the database.
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    /**
     * Constructor for injecting dependencies.
     *
     * @param auditLogRepository the repository for audit log persistence
     * @param auditLogMapper     the mapper for converting events to entity objects
     */
    @Autowired
    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        super(); // Although not necessary in this context, this can be omitted.
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    /**
     * Creates and saves an audit log based on the provided event.
     *
     * @param event the audit log event containing the details to be logged
     * @return the persisted AuditLog entity
     */
    @Override
    public AuditLog createAuditLog(AuditLogEvent event) {
        // Convert the incoming audit log event to an entity using the mapper
        AuditLog auditLog = auditLogMapper.toEntity(event);

        // Save the entity in the database and return the saved entity
        return auditLogRepository.save(auditLog);
    }
}
