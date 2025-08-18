package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.OrderItemDto;
import com.ecommerce.backend.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);
}
