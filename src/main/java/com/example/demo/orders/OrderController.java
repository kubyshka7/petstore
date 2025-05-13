package com.example.demo.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{order_id}")
    public OrderResponse getOrderById(@PathVariable("order_id") Long order_id){
        return orderService.getOrder(order_id);
    }

    @PostMapping
    public void createOrder(
            @RequestBody OrderRequest request
    ){
        orderService.createOrder(request);
    }

    @PutMapping("/{order_id}")
    public void updateOrder(@PathVariable("order_id") Long order_id, @RequestBody OrderRequest request){
        orderService.updateOrder(order_id, request);
    }

    @DeleteMapping("/{order_id}")
    public void deleteOrder(@PathVariable("order_id") Long order_id){
        orderService.deleteOrder(order_id);
    }
}
