package com.example.demo.pets;

import com.example.demo.pets.request.PetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    private static final String NAME_REGEX = "^[A-za-zА-Яа-яЁё]+$";
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Autowired
    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    public List<PetResponse> getAllPets() {
        List<Pet> pets = (petRepository.findAllPets());
        return pets.stream().map(petMapper::toResponse).toList();
    }

    public PetResponse getPet(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be empty");
        }
        return petMapper.toResponse(petRepository.findPetById(id));
    }

    public void createPet(PetRequest request) {
        List<String> errors = new ArrayList<>();
        if ((request.pet_name() == null) || !request.pet_name().matches(NAME_REGEX)) {
            errors.add("Name must have only letters");
        }
        if (request.birthdate().isAfter(LocalDate.now())) {
            errors.add("Birth date must be less than current date");
        }
        if (!(request.pet_owner() == null) && !request.pet_owner().matches(NAME_REGEX)) {
            errors.add("Customer name must have only letters");
        }
        if(!errors.isEmpty()){
            throw new IllegalArgumentException(String.join(", ", errors));
        }
        petRepository.createNewPet(petMapper.toEntity(request));
    }

    public void updatePet(Long id, PetRequest petRequest) {
        Pet pet = petMapper.toEntity(petRequest);
        List<String> errors = new ArrayList<>();

        if (id == null) {
            errors.add("ID must not be empty");
        }
        if ((pet.pet_name() == null) || !pet.pet_name().matches(NAME_REGEX)) {
            errors.add("Name must have only letters");
        }
        if (pet.birthdate().isAfter(LocalDate.now())) {
            errors.add("Birth date must be less than current date");
        }
        if (!(pet.pet_owner() == null) && !pet.pet_owner().matches(NAME_REGEX)) {
            errors.add("Owner name must have only letters");
        }
        if(!errors.isEmpty()){
            throw new IllegalArgumentException(String.join(", ", errors));
        }

        petRepository.changePet(id, pet);
    }

    public void deletePet(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be empty");
        }
        petRepository.deletePet(id);
    }
}
