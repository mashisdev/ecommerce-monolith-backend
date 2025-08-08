package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.CreateAddressRequest;
import com.ecommerce.backend.dto.request.UpdateAddressRequest;
import com.ecommerce.backend.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    // Address <-> CategoryDto
    @Mapping(target = "userId", source = "user.id")
    AddressDto addressToAddressDto(Address address);

    // Create & UpdateAddressRequest -> Address
    Address toEntity(CreateAddressRequest request);
    Address toEntity(UpdateAddressRequest request);
}
