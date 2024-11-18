package com.info.ems.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.models.AuditLog;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AuditLogMapper {
	@Mapping(source = "details",target = "details",qualifiedByName = "mapDetailsField")
	public abstract AuditLog toEntity(AuditLogEvent event);

	@Named("mapDetailsField")
	public String mapDetailsField(Object details)
	{
		return details.toString();
	}
}
