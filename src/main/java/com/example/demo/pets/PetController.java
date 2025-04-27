package com.example.demo.pets;

import com.example.demo.pets.request.PetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets/")
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;

    @Autowired
    public PetController(PetService petService, PetMapper petMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @GetMapping
    public List<PetResponse> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{petId}")
    public PetResponse getPetById(@PathVariable("petId") Long petId){
        return petService.getPet(petId);
    }

    @PostMapping
    public void createPet(
            @RequestBody PetRequest request
    ){
        petService.createPet(request);
    }

    @PutMapping("/{petId}")
    public void updatePet(@PathVariable("petId") Long petId, @RequestBody PetRequest request){
        petService.updatePet(petId, request);
    }

    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable("petId") Long petId){
        petService.deletePet(petId);
    }
}
