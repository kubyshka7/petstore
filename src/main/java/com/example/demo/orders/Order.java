package com.example.demo.orders;

import com.example.demo.pets.Pet;
import com.example.demo.pets.PetRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
    private String customerName;
    private LocalDate orderDate;
    private String status;

    public Order() {
    }

    public Order(Pet pet, String customerName, LocalDate orderDate, String status) {
        this.pet = pet;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Long getId() {return id;}

    public Pet getPet() {return pet;}

    public String getCustomerName() {return customerName;}

    public LocalDate getOrderDate() {return orderDate;}

    public String getStatus() {return status;}

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}


