package com.example.demo.pets;

import com.example.demo.pets.request.PetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (!request.name().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Name must have only letters");
        }
        if (request.birthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date must be less than current date");
        }
        if (!request.owner().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Name must have only letters");
        }
        petRepository.createNewPet(petMapper.toEntity(request));
    }

    public void updatePet(Long id, PetRequest petRequest) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be empty");
        }

        Pet pet = petMapper.toEntity(petRequest);
        List<String> errors = new ArrayList<>();

        if (!pet.getName().matches("^[A-za-zА-Яа-яЁё]+$")) {
            errors.add("Name must have only letters");
        }
        if (pet.getBirthdate().isAfter(LocalDate.now())) {
            errors.add("Birth date must be less than current date");
        }
        if (!pet.getOwner().matches("^[A-za-zА-Яа-яЁё]+$")) {
            errors.add("Name must have only letters");
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
