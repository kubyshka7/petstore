package com.example.demo.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets/")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable("orderId") Long orderId){
        return orderService.getOrder(orderId);
    }

    @PostMapping
    public void createOrder(
            @RequestBody OrderRequest request
    ){
        orderService.createOrder(request);
    }

    @PutMapping("/{orderId}")
    public void updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderRequest request){
        orderService.updateOrder(orderId, request);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable("orderId") Long orderId){
        orderService.deleteOrder(orderId);
    }
}
