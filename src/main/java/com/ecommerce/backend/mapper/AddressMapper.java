package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.AddressRequest;
import com.ecommerce.backend.entity.Address;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    // Address -> AddressDto
    @Mapping(target = "userId", source = "user.id")
    AddressDto addressToAddressDto(Address address);

    // AddressRequest -> Address
    Address toEntity(AddressRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromRequest(AddressRequest request, @MappingTarget Address entity);
}
