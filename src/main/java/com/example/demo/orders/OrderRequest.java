package com.example.demo.orders;

import java.time.LocalDate;

/** Тело запроса на создание или изменение заказа. */
public record OrderRequest(Long petId, String customerName, LocalDate orderDate, String status) {
}
