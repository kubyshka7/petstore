package com.example.demo.pets;

import com.example.demo.common.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PetService {

    private static final String LETTERS = "^[A-Za-zА-Яа-яЁё]+$";

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPet(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet with ID " + id + " is not found"));
    }

    public Pet createPet(Pet pet) {
        if (pet.getId() != null) {
            throw new IllegalArgumentException("ID must be empty");
        }
        validate(pet);
        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new NotFoundException("Pet with ID " + id + " is not found");
        }
        petRepository.deleteById(id);
    }

    public Pet putPet(Long id, Pet pet) {
        Pet existing = getPet(id);
        validate(pet);

        existing.setName(pet.getName());
        existing.setType(pet.getType());
        existing.setBirthdate(pet.getBirthdate());
        existing.setOwner(pet.getOwner());
        return petRepository.save(existing);
    }

    private void validate(Pet pet) {
        if (pet.getName() == null || !pet.getName().matches(LETTERS)) {
            throw new IllegalArgumentException("Pet name must have only letters");
        }
        if (pet.getOwner() == null || !pet.getOwner().matches(LETTERS)) {
            throw new IllegalArgumentException("Owner name must have only letters");
        }
        if (pet.getBirthdate() == null || pet.getBirthdate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date must not be in the future");
        }
    }
}
