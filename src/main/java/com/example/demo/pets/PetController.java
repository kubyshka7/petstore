package com.example.demo.pets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> returnPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public Pet returnPet(@PathVariable Long id) {
        return petService.getPet(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet createPet(@RequestBody Pet pet) {
        return petService.createPet(pet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }

    @PutMapping("/{id}")
    public Pet putPet(@PathVariable Long id, @RequestBody Pet pet) {
        return petService.putPet(id, pet);
    }
}
