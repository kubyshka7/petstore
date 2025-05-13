package com.example.demo.orders;

import java.time.LocalDate;

public record Order(
        Long id,
        Long petId,
        String customerName,
        LocalDate orderDate,
        Status status
) {}