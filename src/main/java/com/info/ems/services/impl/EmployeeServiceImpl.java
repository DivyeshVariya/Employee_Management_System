package com.info.ems.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.ems.constants.AuditLogConstants;
import com.info.ems.constants.Constants;
import com.info.ems.dtos.request.CreateEmployee;
import com.info.ems.dtos.response.EmployeeResponseDto;
import com.info.ems.exceptions.EmployeeAlreadyExistsException;
import com.info.ems.exceptions.EmployeeNotFoundException;
import com.info.ems.kafka.events.AuditLogEvent;
import com.info.ems.kafka.producers.AuditLogProducerService;
import com.info.ems.mapper.AddressMapper;
import com.info.ems.mapper.EmployeeMapper;
import com.info.ems.models.Address;
import com.info.ems.models.Employee;
import com.info.ems.repositories.EmployeeRepository;
import com.info.ems.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressMapper addressMapper;
    private final AuditLogProducerService auditLogKafkaProducer;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                                AddressMapper addressMapper, AuditLogProducerService auditLogKafkaProducer) {
        super();
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.addressMapper = addressMapper;
        this.auditLogKafkaProducer = auditLogKafkaProducer;
    }

    /**
     * Create a new employee along with their addresses.
     * @param request the employee creation request containing employee details and addresses.
     * @return the created employee's details as a DTO.
     */
    @Override
    public EmployeeResponseDto createEmployee(CreateEmployee request) {
        // Check if an employee with the given email already exists
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmployeeAlreadyExistsException("Employee with email " + request.getEmail() + " already exists.");
        }

        // Map CreateEmployee DTO to Employee entity
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());

        // Map addresses to Address entities and associate with the employee
        List<Address> addresses = request.getAddresses().stream().map(addrReq -> {
            Address address = new Address();
            address.setStreet(addrReq.getStreet());
            address.setCity(addrReq.getCity());
            address.setState(addrReq.getState());
            address.setPostalCode(addrReq.getPostalCode());
            address.setEmployee(employee); // Establish bidirectional relationship
            return address;
        }).toList();

        employee.setAddresses(addresses);

        // Save the employee entity
        Employee savedEmployee = employeeRepository.save(employee);

        // Send audit log event to Kafka
        auditLogKafkaProducer.sendAuditLogEventToKafka(
                new AuditLogEvent(AuditLogConstants.CREATE, LocalDateTime.now(), savedEmployee));

        // Return the created employee's details as a DTO
        return employeeMapper.toDto(savedEmployee);
    }

    /**
     * Get an employee by their ID.
     * @param id the ID of the employee.
     * @return the employee's details as a DTO.
     */
    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        return employeeMapper.toDto(
                employeeRepository.findById(id).orElseThrow(() -> 
                        new EmployeeNotFoundException("Employee with id " + id + " not found."))
        );
    }

    /**
     * Get the Employee entity by ID.
     * This method is used internally to fetch the entity for updates or deletions.
     * @param id the ID of the employee.
     * @return the Employee entity.
     */
    public Employee getEmployeeByEmployeeId(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> 
                new EmployeeNotFoundException("Employee with id " + id + " not found."));
    }

    /**
     * Update an existing employee's details along with their addresses.
     * @param id the ID of the employee to update.
     * @param request the employee update request containing new details.
     * @return the updated employee's details as a DTO.
     */
    @Override
    public EmployeeResponseDto updateEmployee(Long id, CreateEmployee request) {
        // Fetch the employee entity by ID
        Employee employee = getEmployeeByEmployeeId(id);

        // Update employee details
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());

        // Update addresses
        List<Address> addresses = request.getAddresses().stream().map(addrReq -> {
            Address address = new Address();
            address.setStreet(addrReq.getStreet());
            address.setCity(addrReq.getCity());
            address.setState(addrReq.getState());
            address.setPostalCode(addrReq.getPostalCode());
            address.setEmployee(employee); // Maintain bidirectional relationship
            return address;
        }).toList();

        employee.setAddresses(addresses);

        // Save the updated employee entity
        Employee updatedEmployee = employeeRepository.save(employee);

        // Send audit log event to Kafka
        auditLogKafkaProducer.sendAuditLogEventToKafka(
                new AuditLogEvent(AuditLogConstants.UPDATE, LocalDateTime.now(), updatedEmployee));

        // Return the updated employee's details as a DTO
        return employeeMapper.toDto(updatedEmployee);
    }

    /**
     * Delete an employee by ID.
     * @param id the ID of the employee to delete.
     * @return a map containing confirmation of the deletion.
     */
    @Override
    public Map<String, Object> deleteEmployee(Long id) {
        // Fetch the employee entity by ID
        Employee employee = getEmployeeByEmployeeId(id);

        // Delete the employee entity
        employeeRepository.delete(employee);

        // Send audit log event to Kafka
        auditLogKafkaProducer.sendAuditLogEventToKafka(
                new AuditLogEvent(AuditLogConstants.DELETE, LocalDateTime.now(), employee));

        // Return confirmation of the deletion
        return Map.of(Constants.EMPLOYEE_ID, id, Constants.DELETED, Boolean.TRUE);
    }
}
