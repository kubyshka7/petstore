package com.example.demo.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> returnOrders() {return orderService.getAllOrders();}

    @GetMapping("/{orderId}")
    public Order returnOrder(@PathVariable("orderId") Long id){
        return orderService.getOrder(id);
    }

    @PostMapping
    public Order createOrder(
            @PathVariable("petId") Long petId, String customerName, LocalDate orderDate, String status
    ){
        return orderService.createOrder(petId, customerName, orderDate, status);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable("orderId") Long id){
        orderService.deleteOrder(id);
    }

    @PutMapping("/{orderId}")
    public void putOrder(@PathVariable("orderId") Long id, @PathVariable("petId") Long petId, String customerName, LocalDate orderDate, String status){
        orderService.putOrder(id, petId, customerName, orderDate, status);
    }
}
