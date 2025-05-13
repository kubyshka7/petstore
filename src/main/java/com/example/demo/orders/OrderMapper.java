package com.example.demo.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "status", defaultValue = "CREATED")
    Order toEntity(OrderRequest request);
    @Mapping(source = "status", target = "orderStatus")
    OrderResponse toResponse(Order order);
}
