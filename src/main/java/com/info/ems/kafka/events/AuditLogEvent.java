package com.info.ems.kafka.events;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogEvent {
    @NotBlank(message = "operation is required")
	private String operation;
    @NotBlank(message = "timestamp is required")
    private LocalDateTime timestamp;
    @NotNull(message = "details is required")
    private Object details;
}
