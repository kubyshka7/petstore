package com.example.demo.orders;

import com.example.demo.common.NotFoundException;
import com.example.demo.pets.Pet;
import com.example.demo.pets.PetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private static final String LETTERS = "^[A-Za-zА-Яа-яЁё]+$";

    private final OrderRepository orderRepository;
    private final PetRepository petRepository;

    public OrderService(OrderRepository orderRepository, PetRepository petRepository) {
        this.orderRepository = orderRepository;
        this.petRepository = petRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with ID " + id + " is not found"));
    }

    public Order createOrder(OrderRequest request) {
        validate(request);
        Pet pet = findPet(request.petId());
        // у заказа связь «один к одному» с питомцем: одного питомца нельзя заказать дважды
        if (orderRepository.existsByPetId(pet.getId())) {
            throw new IllegalArgumentException("This pet already has an order");
        }
        Order order = new Order(pet, request.customerName(), request.orderDate(), request.status());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order with ID " + id + " is not found");
        }
        orderRepository.deleteById(id);
    }

    public Order putOrder(Long id, OrderRequest request) {
        Order order = getOrder(id);
        validate(request);
        Pet pet = findPet(request.petId());
        if (orderRepository.existsByPetIdAndIdNot(pet.getId(), id)) {
            throw new IllegalArgumentException("This pet already has an order");
        }

        order.setPet(pet);
        order.setCustomerName(request.customerName());
        order.setOrderDate(request.orderDate());
        order.setStatus(request.status());
        return orderRepository.save(order);
    }

    private Pet findPet(Long petId) {
        if (petId == null) {
            throw new IllegalArgumentException("Pet ID must not be empty");
        }
        return petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet with ID " + petId + " is not found"));
    }

    private void validate(OrderRequest request) {
        if (request.customerName() == null || !request.customerName().matches(LETTERS)) {
            throw new IllegalArgumentException("Customer name must have only letters");
        }
        if (request.orderDate() == null || request.orderDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Order date must not be in the future");
        }
    }
}
