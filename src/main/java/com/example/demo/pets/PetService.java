package com.example.demo.pets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public Pet getPet(Long id){
        if(id==null){
            throw new IllegalArgumentException("ID must not be empty");
        }
        if(petRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Pet with this ID is not found");
        }
        return petRepository.findById(id).get();
    }

    public Pet createPet(Pet pet) {
        if(pet.getId()!=null){
            throw new IllegalArgumentException("ID must be empty");
        }
        if (!pet.getName().matches("^[A-za-zА-Яа-яЁё]+$")){
            throw new IllegalArgumentException("Name must have only letters");
        }
        if (pet.getBirthdate().getDayOfYear() >= LocalDate.now().getDayOfYear()){
            throw new IllegalArgumentException("Birth date must be less than current date");
        }
        if (!pet.getOwner().matches("^[A-za-zА-Яа-яЁё]+$")){
            throw new IllegalArgumentException("Name must have only letters");
        }
        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        if(petRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Pet with this ID is not found");
        }
        petRepository.deleteById(id);
    }

    public void putPet(Long id, Pet pet) {
        if(id==null){
            throw new IllegalArgumentException("ID must not be empty");
        }
        if(petRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Pet with this ID is not found");
        }
        if(pet.getId()==null){
            throw new IllegalArgumentException("pet ID must not be empty");
        }
        if (!pet.getName().matches("^[A-za-zА-Яа-яЁё]+$")){
            throw new IllegalArgumentException("Name must have only letters");
        }
        if (pet.getBirthdate().getDayOfYear() >= LocalDate.now().getDayOfYear()){
            throw new IllegalArgumentException("Birth date must be less than current date");
        }
        if (!pet.getOwner().matches("^[A-za-zА-Яа-яЁё]+$")){
            throw new IllegalArgumentException("Name must have only letters");
        }
        Pet newpet = petRepository.findById(id).get();
        newpet.setBirthdate(pet.getBirthdate());
        newpet.setName(pet.getName());
        newpet.setType(pet.getType());
        newpet.setOwner(pet.getOwner());

        petRepository.save(newpet);
    }
}
