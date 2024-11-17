package com.info.ems.services;

import java.util.Map;

import com.info.ems.dtos.request.CreateEmployee;
import com.info.ems.dtos.response.EmployeeResponseDto;

import jakarta.validation.Valid;

public interface EmployeeService {

	EmployeeResponseDto createEmployee(@Valid CreateEmployee request);
	
	EmployeeResponseDto getEmployeeById(Long id);
	
	EmployeeResponseDto updateEmployee(Long id,@Valid CreateEmployee request);
	
	Map<String,Object> deleteEmployee(Long id);
}
