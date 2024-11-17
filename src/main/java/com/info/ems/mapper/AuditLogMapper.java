package com.info.ems.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.models.AuditLog;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuditLogMapper {
	AuditLog toEntity(AuditLogEvent event);
}
