package com.example.demo.pets;

import com.example.demo.pets.request.PetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<PetResponse> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{pet_id}")
    public PetResponse getPetById(@PathVariable("pet_id") Long pet_id){
        return petService.getPet(pet_id);
    }

    @PostMapping
    public void createPet(
            @RequestBody PetRequest request
    ){
        petService.createPet(request);
    }

    @PutMapping("/{pet_id}")
    public void updatePet(@PathVariable("pet_id") Long pet_id, @RequestBody PetRequest request){
        petService.updatePet(pet_id, request);
    }

    @DeleteMapping("/{pet_id}")
    public void deletePet(@PathVariable("pet_id") Long pet_id){
        petService.deletePet(pet_id);
    }
}
