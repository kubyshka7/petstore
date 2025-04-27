package com.example.demo.orders;

import java.time.LocalDate;

public record OrderResponse(
        Long id,
        Long petId,
        String customerName,
        LocalDate orderDate,
        Status orderStatus
) {}
