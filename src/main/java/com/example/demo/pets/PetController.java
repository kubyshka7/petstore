package com.example.demo.pets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> returnPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public Pet returnPet(@PathVariable("petId") Long id){
        return petService.getPet(id);
    }

    @PostMapping
    public Pet createPet(
            @RequestBody Pet pet
    ){
        return petService.createPet(pet);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable("petId") Long id){
        petService.deletePet(id);
    }

    @PutMapping("/{id}")
    public void putPet(@PathVariable("petId") Long id, @RequestBody Pet pet){
        petService.putPet(id, pet);
    }
}
