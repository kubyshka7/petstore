package com.example.demo.orders;

import com.example.demo.pets.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private static final String NAME_REGEX = "^[A-za-zА-Яа-яЁё]+$";
    private final OrderRepository orderRepository;
    private final PetRepository petRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, PetRepository petRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.petRepository = petRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderResponse> getAllOrders(){
        List<Order> orders = (orderRepository.findAllOrders());
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    public OrderResponse getOrder(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be empty");
        }
        return orderMapper.toResponse(orderRepository.findOrderById(id));
    }

    public void createOrder(OrderRequest request) {
        List<String> errors = new ArrayList<>();

        if (petRepository.findPetById(request.petId()) == null) {
            errors.add("Pet with this id is not exists");
        }
        if (!(orderRepository.findOrderByPetId(request.petId()) == null)) {
            errors.add("Order with this pet already exists");
        }
        if (!request.customerName().matches(NAME_REGEX)) {
            errors.add("Customer name must have only letters");
        }
        if (request.orderDate().isAfter(LocalDate.now())) {
            errors.add("Order date must be less than current date");
        }
        if(!errors.isEmpty()){
            throw new IllegalArgumentException(String.join(", ", errors));
        }
        orderRepository.createNewOrder(orderMapper.toEntity(request));
    }

    public void updateOrder(Long orderId, OrderRequest request) {
        List<String> errors = new ArrayList<>();
        Order order = orderMapper.toEntity(request);

        if (orderId == null) {
            errors.add("ID must not be empty");
        }
        if (petRepository.findPetById(request.petId()) == null) {
            errors.add("Pet with this id is not exist");
        }
        if (!order.customerName().matches(NAME_REGEX)) {
            errors.add("Name must have only letters");
        }
        if (order.orderDate().isAfter(LocalDate.now())) {
            errors.add("Order date must be less than current date");
        }
        if(!errors.isEmpty()){
            throw new IllegalArgumentException(String.join(", ", errors));
        }

        orderRepository.changeOrder(orderId, request);
    }

    public void deleteOrder(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be empty");
        }
        orderRepository.deleteOrder(id);
    }
}
