package com.info.ems.services;

import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.models.AuditLog;

import jakarta.validation.Valid;

public interface AuditLogService {
	AuditLog createAuditLog(@Valid AuditLogEvent event);
}
