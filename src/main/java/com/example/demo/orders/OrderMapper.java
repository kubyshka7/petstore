package com.example.demo.orders;

import com.example.demo.orders.OrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequest request);
    OrderResponse toResponse(Order order);
}
