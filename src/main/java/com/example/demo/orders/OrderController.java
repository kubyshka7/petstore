package com.example.demo.orders;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> returnOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order returnOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @PutMapping("/{orderId}")
    public Order putOrder(@PathVariable Long orderId, @RequestBody OrderRequest request) {
        return orderService.putOrder(orderId, request);
    }
}
