package com.example.demo.orders;

import java.time.LocalDate;

enum Status{
    CREATED,
    PROCESSING,
    COMPLETED
}

public class Order {
    private Long id;
    private Long petId;
    private String customerName;
    private LocalDate orderDate;
    private Status orderStatus;


    public Order() {
    }

    public Order(Long petId, String customerName, LocalDate orderDate) {
        this.petId = petId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.orderStatus = Status.CREATED;
    }

    public Long getId() {return id;}

    public Long getPetId() {return petId;}

    public String getCustomerName() {return customerName;}

    public LocalDate getOrderDate() {return orderDate;}

    public Status getStatus() {return orderStatus;}
}


