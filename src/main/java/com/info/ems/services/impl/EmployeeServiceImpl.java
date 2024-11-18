package com.info.ems.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.info.ems.repositories.AddressRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final AuditLogProducerService auditLogKafkaProducer;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                               AddressMapper addressMapper, AddressRepository addressRepository, AuditLogProducerService auditLogKafkaProducer) {
        super();
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.addressMapper = addressMapper;
      this.addressRepository = addressRepository;
      this.auditLogKafkaProducer = auditLogKafkaProducer;
    }

    /**
     * Create a new employee along with their addresses.
     * @param request the employee creation request containing employee details and addresses.
     * @return the created employee's details as a DTO.
     */
    @Override
    public EmployeeResponseDto createEmployee(CreateEmployee request) {
        log.trace("Inside createEmployee method");
        // Check if an employee with the given email already exists
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmployeeAlreadyExistsException("Employee with email " + request.getEmail() + " already exists.");
        }

        // Map CreateEmployee DTO to Employee entity
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setHireDate(LocalDate.now());

        // Map addresses to Address entities and associate with the employee
        List<Address> addresses = request.getAddresses().stream().map(addrReq -> {
            Address address = new Address();
            address.setStreet(addrReq.getStreet());
            address.setCity(addrReq.getCity());
            address.setState(addrReq.getState());
            address.setPostalCode(addrReq.getPostalCode());
            return address;
        }).toList();
        employee.setAddresses(addresses);
        // Save the employee entity
        Employee savedEmployee = employeeRepository.save(employee);

        log.trace("Saved employee :[{}]",savedEmployee);
        log.trace("Sending kafka event for create...");
        // Send audit log event to Kafka
        auditLogKafkaProducer.sendAuditLogEventToKafka(
                new AuditLogEvent(AuditLogConstants.CREATE, LocalDateTime.now(), savedEmployee));
        log.trace("Sent kafka event for create...");

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
        log.trace("Inside getEmployeeById method");
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
        log.trace("Inside getEmployeeByEmployeeId method");
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
        log.trace("Inside updateEmployee method");
        // Fetch the employee entity by ID
        Employee employee = getEmployeeByEmployeeId(id);
        log.trace("Fetch employee : [{}]", employee);
        // Update employee details
        employee.setId(employee.getId());
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());

        // Update addresses
        List<Address> addresses = employee.getAddresses();
        request.getAddresses()
               .forEach(addrReq -> {
            Address address = new Address();
            address.setStreet(addrReq.getStreet());
            address.setCity(addrReq.getCity());
            address.setState(addrReq.getState());
            address.setPostalCode(addrReq.getPostalCode());
            addresses.add(address);
        });
        log.trace("After prepare address list...");
        employee.setAddresses(addresses);

        // Save the updated employee entity
        Employee updatedEmployee = employeeRepository.save(employee);
        log.trace("Updated employee :[{}]",updatedEmployee);
        log.trace("Sending kafka event for update...");
        // Send audit log event to Kafka
        auditLogKafkaProducer.sendAuditLogEventToKafka(
                new AuditLogEvent(AuditLogConstants.UPDATE, LocalDateTime.now(), updatedEmployee));
        log.trace("Sent kafka event for update...");

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
        log.trace("Inside deleteEmployee method");
        // Fetch the employee entity by ID
        Employee employee = getEmployeeByEmployeeId(id);

        // Delete the employee entity
        employeeRepository.delete(employee);
        log.trace("Deleted employee :[{}]",employee);
        log.trace("Sending kafka event for delete...");
        // Send audit log event to Kafka
        auditLogKafkaProducer.sendAuditLogEventToKafka(
                new AuditLogEvent(AuditLogConstants.DELETE, LocalDateTime.now(), employee));
        log.trace("Sent kafka event for delete...");

        // Return confirmation of the deletion
        return Map.of(Constants.EMPLOYEE_ID, id, Constants.DELETED, Boolean.TRUE);
    }
}
