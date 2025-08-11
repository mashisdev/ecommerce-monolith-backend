package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.address.CreateAddressRequest;
import com.ecommerce.backend.dto.request.address.UpdateAddressRequest;
import com.ecommerce.backend.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    // Address <-> AddressDto
    @Mapping(target = "userId", source = "user.id")
    AddressDto addressToAddressDto(Address address);

    // Create & UpdateAddressRequest -> Address
    Address toEntity(CreateAddressRequest request);
    Address toEntity(UpdateAddressRequest request);
}
