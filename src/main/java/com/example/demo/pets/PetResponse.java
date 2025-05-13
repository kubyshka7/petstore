package com.example.demo.pets;

import java.time.LocalDate;

public record PetResponse(
        Long pet_id,
        String pet_name,
        String pet_type,
        LocalDate birthdate,
        String pet_owner
) {}
