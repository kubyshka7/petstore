package com.example.demo.orders;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-13T22:30:34+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Status status = null;
        Long petId = null;
        String customerName = null;
        LocalDate orderDate = null;

        if ( request.status() != null ) {
            status = request.status();
        }
        else {
            status = Status.CREATED;
        }
        petId = request.petId();
        customerName = request.customerName();
        orderDate = request.orderDate();

        Long id = null;

        Order order = new Order( id, petId, customerName, orderDate, status );

        return order;
    }

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        Status orderStatus = null;
        Long id = null;
        Long petId = null;
        String customerName = null;
        LocalDate orderDate = null;

        orderStatus = order.status();
        id = order.id();
        petId = order.petId();
        customerName = order.customerName();
        orderDate = order.orderDate();

        OrderResponse orderResponse = new OrderResponse( id, petId, customerName, orderDate, orderStatus );

        return orderResponse;
    }
}
