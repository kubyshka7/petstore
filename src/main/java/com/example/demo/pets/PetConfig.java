package com.example.demo.pets;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class PetConfig {

    @Bean
    CommandLineRunner commandLineRunner(PetRepository petRepository){
        return (args) -> {
            var petList = List.of(
                    new Pet(null,
                            "Fufik",
                            "dog",
                            LocalDate.of(2025, 3, 10),
                            "Pasha"),
                    new Pet(
                            null,
                            "Masik",
                            "cat",
                            LocalDate.of(2024, 8, 10),
                            "Sasha"
                    )
            );
            petRepository.saveAll(petList);
        };
    }
}
