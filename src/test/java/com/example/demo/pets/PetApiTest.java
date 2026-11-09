package com.example.demo.pets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Проверка API целиком на базе H2: приложение поднимается с тестовыми питомцами из PetConfig. */
@SpringBootTest
@AutoConfigureMockMvc
class PetApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listsSeededPets() throws Exception {
        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void returnsPetById() throws Exception {
        mvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fufik"));
    }

    @Test
    void unknownPetGives404() throws Exception {
        mvc.perform(get("/api/pets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidPetGives400() throws Exception {
        mvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Rex1\",\"type\":\"dog\",\"birthdate\":\"2024-01-01\",\"owner\":\"Anna\"}"))
                .andExpect(status().isBadRequest());
    }
}
