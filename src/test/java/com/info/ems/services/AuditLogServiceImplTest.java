package com.info.ems.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.mapper.AuditLogMapper;
import com.info.ems.models.AuditLog;
import com.info.ems.repositories.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    @InjectMocks
    private AuditLogService auditLogService;

    private AuditLogEvent auditLogEvent;
    private AuditLog auditLogEntity;

    @BeforeEach
    public void setUp() {
        // Mock the audit log event that will be passed to the service
        auditLogEvent = new AuditLogEvent("CREATE", LocalDateTime.now(), "Employee Created");

        // Mock the audit log entity that will be returned after saving
        auditLogEntity = new AuditLog();
        auditLogEntity.setEvent("CREATE");
        auditLogEntity.setTimestamp(auditLogEvent.getTimestamp());
        auditLogEntity.setDetails("Employee Created");

        // Mock the mapper to return the AuditLog entity from the event
        when(auditLogMapper.toEntity(auditLogEvent)).thenReturn(auditLogEntity);

        // Mock the repository to save and return the AuditLog entity
        when(auditLogRepository.save(auditLogEntity)).thenReturn(auditLogEntity);
    }

    @Test
    public void testCreateAuditLog_Success() {
        // Call the method under test
        AuditLog result = auditLogService.createAuditLog(auditLogEvent);

        // Verify the interactions and assert the result
        verify(auditLogMapper).toEntity(auditLogEvent);  // Ensure mapper is called
        verify(auditLogRepository).save(auditLogEntity); // Ensure the entity is saved

        // Assert that the returned entity matches the expected result
        assertThat(result).isNotNull();
        assertThat(result.getEvent()).isEqualTo("CREATE");
        assertThat(result.getTimestamp()).isEqualTo(auditLogEvent.getTimestamp());
        assertThat(result.getDetails()).isEqualTo("Employee Created");
    }

    @Test
    public void testCreateAuditLog_Failure_EntityNotSaved() {
        // Simulate repository failure by making save return null
        when(auditLogRepository.save(auditLogEntity)).thenReturn(null);

        // Call the method under test
        AuditLog result = auditLogService.createAuditLog(auditLogEvent);

        // Assert that the result is null, indicating save failure
        assertThat(result).isNull();
    }
}
