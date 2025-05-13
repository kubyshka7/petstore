package com.example.demo.pets;

import com.example.demo.pets.request.PetRequest;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T22:54:16+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class PetMapperImpl implements PetMapper {

    @Override
    public Pet toEntity(PetRequest request) {
        if ( request == null ) {
            return null;
        }

        String pet_name = null;
        String pet_type = null;
        LocalDate birthdate = null;
        String pet_owner = null;

        pet_name = request.pet_name();
        pet_type = request.pet_type();
        birthdate = request.birthdate();
        pet_owner = request.pet_owner();

        Long pet_id = null;

        Pet pet = new Pet( pet_id, pet_name, pet_type, birthdate, pet_owner );

        return pet;
    }

    @Override
    public PetResponse toResponse(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        Long pet_id = null;
        String pet_name = null;
        String pet_type = null;
        LocalDate birthdate = null;
        String pet_owner = null;

        pet_id = pet.pet_id();
        pet_name = pet.pet_name();
        pet_type = pet.pet_type();
        birthdate = pet.birthdate();
        pet_owner = pet.pet_owner();

        PetResponse petResponse = new PetResponse( pet_id, pet_name, pet_type, birthdate, pet_owner );

        return petResponse;
    }
}
