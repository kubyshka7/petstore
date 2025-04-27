package com.example.demo.pets;

import java.time.LocalDate;

public record PetResponse(
        Long id,
        String name,
        String type,
        LocalDate birthdate
) {}
