package com.info.ems.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.info.ems.dtos.request.CreateAddress;
import com.info.ems.models.Address;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
	Address toEntity(CreateAddress request);
}
