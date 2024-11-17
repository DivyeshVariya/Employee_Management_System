package com.info.ems.dtos.response;

import java.util.List;
import com.info.ems.models.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDto {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private List<Address> addresses;
}
