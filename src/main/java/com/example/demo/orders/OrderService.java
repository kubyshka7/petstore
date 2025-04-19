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
        return orderRepository.findAll();
    }

    public Order getOrder(@PathVariable Long id) {
        if(id==null){
            throw new IllegalArgumentException("ID must not be empty");
        }
        if(orderRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Order with this ID is not found");
        }
        return orderRepository.findById(id).get();
    }

    public Order createOrder(Long petId, String customerName, LocalDate orderDate, String status) {
        if (petRepository.findById(petId).isEmpty()){
            throw new IllegalArgumentException("Pet must exist");
        }
        if (orderDate.getDayOfYear() >= LocalDate.now().getDayOfYear()){
            throw new IllegalArgumentException("Order date must be less than current date");
        }
        if (!customerName.matches("^[A-za-zА-Яа-яЁё]+$")){
            throw new IllegalArgumentException("Name must have only letters");
        }
        Order order = new Order(petRepository.findById(petId).get(), customerName, orderDate, status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (petRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Pet must exist");
        }
        orderRepository.deleteById(id);
    }

    public void putOrder(Long id, Long petId, String customerName, LocalDate orderDate, String status) {
        if(id==null){
            throw new IllegalArgumentException("ID must not be empty");
        }
        if(orderRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Order with this ID is not found");
        }
        if (petRepository.findById(petId).isEmpty()){
            throw new IllegalArgumentException("Pet must exist");
        }
        if (orderDate.getDayOfYear() >= LocalDate.now().getDayOfYear()){
            throw new IllegalArgumentException("Order date must be less than current date");
        }
        if (!customerName.matches("^[A-za-zА-Яа-яЁё]+$")){
            throw new IllegalArgumentException("Name must have only letters");
        }
        Order order = orderRepository.findById(id).get();
        order.setOrderDate(orderDate);
        order.setCustomerName(customerName);
        order.setPet(petRepository.findById(petId).get());
        order.setStatus(status);

        orderRepository.save(order);
    }
}
