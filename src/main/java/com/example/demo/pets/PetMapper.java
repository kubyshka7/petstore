package com.example.demo.pets;

import com.example.demo.pets.request.PetRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {
    Pet toEntity(PetRequest request);
    PetResponse toResponse(Pet pet);
}
