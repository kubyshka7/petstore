package com.example.demo.pets.request;

import java.time.LocalDate;

public record PetRequest(
        String name,
        String type,
        LocalDate birthDate,
        String owner
) {}
