package com.info.ems.services;

import com.info.ems.dtos.request.CreateEmployee;
import com.info.ems.dtos.response.EmployeeResponseDto;
import com.info.ems.exceptions.EmployeeAlreadyExistsException;
import com.info.ems.exceptions.EmployeeNotFoundException;
import com.info.ems.kafka.producers.AuditLogProducerService;
import com.info.ems.mapper.EmployeeMapper;
import com.info.ems.models.Employee;
import com.info.ems.repositories.EmployeeRepository;
import com.info.ems.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private AuditLogProducerService auditLogProducerService;

    @InjectMocks
    private EmployeeService employeeService;

    private CreateEmployee createEmployeeRequest;
    private EmployeeResponseDto employeeResponseDto;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createEmployeeRequest = new CreateEmployee("John Doe", "john.doe@example.com", "1234567890", null);
        employeeResponseDto = new EmployeeResponseDto(1L, "John Doe", "john.doe@example.com", "1234567890");
        employee = new Employee(1L, "John Doe", "john.doe@example.com", "1234567890", null);
    }

    @Test
    void testCreateEmployee_Success() {
        // Arrange
        when(employeeRepository.findByEmail(createEmployeeRequest.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeResponseDto);

        // Act
        EmployeeResponseDto response = employeeService.createEmployee(createEmployeeRequest);

        // Assert
        verify(employeeRepository).save(any(Employee.class));
        verify(auditLogProducerService).sendAuditLogEventToKafka(any());
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    void testCreateEmployee_EmployeeAlreadyExists() {
        // Arrange
        when(employeeRepository.findByEmail(createEmployeeRequest.getEmail())).thenReturn(Optional.of(employee));

        // Act & Assert
        EmployeeAlreadyExistsException thrown = 
            assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.createEmployee(createEmployeeRequest));
        assertEquals("Employee with email john.doe@example.com already exists.", thrown.getMessage());
    }

    @Test
    void testGetEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeResponseDto);

        // Act
        EmployeeResponseDto response = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
    }

    @Test
    void testGetEmployeeById_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EmployeeNotFoundException thrown = 
            assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
        assertEquals("Employee with id 1 not found.", thrown.getMessage());
    }

    @Test
    void testUpdateEmployee_Success() {
        // Arrange
        CreateEmployee updateRequest = new CreateEmployee("John Doe Updated", "john.doe.updated@example.com", "0987654321", null);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeResponseDto);

        // Act
        EmployeeResponseDto response = employeeService.updateEmployee(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe Updated", response.getName());
    }

    @Test
    void testUpdateEmployee_EmployeeNotFound() {
        // Arrange
        CreateEmployee updateRequest = new CreateEmployee("John Doe Updated", "john.doe.updated@example.com", "0987654321", null);
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EmployeeNotFoundException thrown = 
            assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(1L, updateRequest));
        assertEquals("Employee with id 1 not found.", thrown.getMessage());
    }

    @Test
    void testDeleteEmployee_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Map<String, Object> response = employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository).delete(employee);
        assertEquals(Boolean.TRUE, response.get("deleted"));
    }

    @Test
    void testDeleteEmployee_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EmployeeNotFoundException thrown = 
            assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(1L));
        assertEquals("Employee with id 1 not found.", thrown.getMessage());
    }
}
