package com.example.demo.pets;

import java.time.LocalDate;

// todo почитать про record в java
public record Pet (
    Long pet_id,
    String pet_name,
    String pet_type,
    LocalDate birthdate,
    String pet_owner
) {}
