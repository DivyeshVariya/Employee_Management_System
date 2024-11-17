package com.info.ems.controller;

import com.info.ems.dtos.request.CreateEmployee;
import com.info.ems.dtos.response.EmployeeResponseDto;
import com.info.ems.dtos.response.Response;
import com.info.ems.services.EmployeeService;
import com.info.ems.constants.Constants;
import com.info.ems.contollers.EmployeeController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test cases for EmployeeController class that test all CRUD operations for employee management.
 */
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testCreateEmployee() throws Exception {
        CreateEmployee request = new CreateEmployee();
        request.setName("John Doe");
        request.setPosition("Software Engineer");

        EmployeeResponseDto responseDto = new EmployeeResponseDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setPosition("Software Engineer");

        when(employeeService.createEmployee(request)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/ems/create-employee")
                        .contentType("application/json")
                        .content("{ \"name\": \"John Doe\", \"position\": \"Software Engineer\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("Employee created successfully"))
                .andExpect(jsonPath("$.data").exists());

        verify(employeeService, times(1)).createEmployee(request);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Long id = 1L;
        EmployeeResponseDto responseDto = new EmployeeResponseDto();
        responseDto.setId(id);
        responseDto.setName("John Doe");
        responseDto.setPosition("Software Engineer");

        when(employeeService.getEmployeeById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/ems/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Employee found successfully"))
                .andExpect(jsonPath("$.data").exists());

        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Long id = 1L;
        
        CreateEmployee request = new CreateEmployee();
        request.setName("John Updated");
        request.setPosition("Senior Software Engineer");

        EmployeeResponseDto responseDto = new EmployeeResponseDto();
        responseDto.setId(id);
        responseDto.setName("John Updated");
        responseDto.setPosition("Senior Software Engineer");

        when(employeeService.updateEmployee(id, request)).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/ems/{id}", id)
                        .contentType("application/json")
                        .content("{ \"name\": \"John Updated\", \"position\": \"Senior Software Engineer\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Employee updated successfully"))
                .andExpect(jsonPath("$.data").exists());

        verify(employeeService, times(1)).updateEmployee(id, request);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Long id = 1L;
        Map<String, Object> responseMap = Map.of(Constants.DATA, "Employee deleted successfully");

        when(employeeService.deleteEmployee(id)).thenReturn(responseMap);

        mockMvc.perform(delete("/api/v1/ems/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"))
                .andExpect(jsonPath("$.data").exists());

        verify(employeeService, times(1)).deleteEmployee(id);
    }
}
