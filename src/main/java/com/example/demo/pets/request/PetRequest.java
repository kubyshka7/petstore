package com.example.demo.pets.request;

import java.time.LocalDate;

public record PetRequest(
        String pet_name,
        String pet_type,
        LocalDate birthdate,
        String pet_owner
) {}
