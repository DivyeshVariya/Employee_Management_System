package com.info.ems.contollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.info.ems.constants.Constants;
import com.info.ems.dtos.request.CreateEmployee;
import com.info.ems.dtos.response.EmployeeResponseDto;
import com.info.ems.dtos.response.Response;
import com.info.ems.services.EmployeeService;

import jakarta.validation.Valid;

/**
 * EmployeeController class handles HTTP requests related to employee management, including
 * operations like creating, retrieving, updating, and deleting employee records.
 * <p>
 * All endpoints in this controller are prefixed with /api/v1/ems and support Cross-Origin Resource Sharing (CORS).
 * </p>
 */
@RestController
@RequestMapping("/api/v1/ems")
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Constructor to initialize the EmployeeService dependency.
     * 
     * @param employeeService The service responsible for employee-related business logic.
     */
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Endpoint to create a new employee.
     * 
     * @param request The employee details to be created.
     * @return A ResponseEntity containing the created employee data.
     */
    @PostMapping("/create-employee")
    public ResponseEntity<Response> createEmployee(@RequestBody @Valid CreateEmployee request) {
        // Create the employee and fetch the saved details
        EmployeeResponseDto savedEmployee = employeeService.createEmployee(request);
        
        // Build and return the response with employee data
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        Response
                                .builder()
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .data(Map.of(Constants.DATA, savedEmployee))
                                .message("Employee created successfully")
                                .build());
    }

    /**
     * Endpoint to retrieve an employee by their ID.
     * 
     * @param id The unique ID of the employee.
     * @return A ResponseEntity containing the employee details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response> getEmployeeById(@PathVariable Long id) {
        // Fetch employee details by ID
        EmployeeResponseDto employee = employeeService.getEmployeeById(id);
        
        // Build and return the response with employee data
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Response
                                .builder()
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of(Constants.DATA, employee))
                                .message("Employee found successfully")
                                .build());    
    }

    /**
     * Endpoint to update an existing employee's details.
     * 
     * @param id      The ID of the employee to update.
     * @param request The updated employee details.
     * @return A ResponseEntity containing the updated employee data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateEmployee(@PathVariable Long id, @RequestBody @Valid CreateEmployee request) {
        // Update the employee and fetch the updated details
        EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, request);
        
        // Build and return the response with updated employee data
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Response
                                .builder()
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of(Constants.DATA, updatedEmployee))
                                .message("Employee updated successfully")
                                .build());    
    }

    /**
     * Endpoint to delete an employee by their ID.
     * 
     * @param id The ID of the employee to delete.
     * @return A ResponseEntity containing a confirmation message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEmployee(@PathVariable Long id) {
        // Delete the employee and get the deletion status
        Map<String,Object> deleted = employeeService.deleteEmployee(id);
        
        // Build and return the response with deletion confirmation
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Response
                                .builder()
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of(Constants.DATA, deleted))
                                .message("Employee deleted successfully.")
                                .build());
    }
}
