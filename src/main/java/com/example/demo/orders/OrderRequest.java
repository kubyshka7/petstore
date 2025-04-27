package com.example.demo.orders;

import java.time.LocalDate;

public record OrderRequest(
        Long petId,
        String customerName,
        LocalDate orderDate,
        Status orderStatus
) {}
