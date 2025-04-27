package com.example.demo.orders;

import com.example.demo.pets.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PetRepository petRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, PetRepository petRepository) {
        this.orderRepository = orderRepository;
        this.petRepository = petRepository;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAllOrders();
    }

    public Order getOrder(@PathVariable Long id) {

    }

    // todo проверки вынести в метод (почитай про fail fast и fail safe)
    public Order createOrder(Long petId, String customerName, LocalDate orderDate, String status) {

    }

    public void updateOrder(Long orderId, OrderRequest request) {
    }

    public void deleteOrder(Long id) {

    }
}
