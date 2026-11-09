package com.example.demo.orders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // каждый тест откатывается, база остаётся с исходными данными
class OrderApiTest {

    @Autowired
    MockMvc mvc;

    private String order(long petId, String customer) {
        return """
                {"petId": %d, "customerName": "%s", "orderDate": "%s", "status": "new"}
                """.formatted(petId, customer, LocalDate.now());
    }

    @Test
    void createsOrder() throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(order(1, "Ivan")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pet.name").value("Fufik"))
                .andExpect(jsonPath("$.customerName").value("Ivan"));
    }

    @Test
    void secondOrderForSamePetGives400() throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(order(1, "Ivan")))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(order(1, "Petr")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void orderForUnknownPetGives404() throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(order(999, "Ivan")))
                .andExpect(status().isNotFound());
    }
}
