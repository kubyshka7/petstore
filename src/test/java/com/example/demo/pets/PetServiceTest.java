package com.example.demo.pets;

import com.example.demo.common.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    PetService petService;

    @Test
    void createPetSavesValidPet() {
        Pet pet = new Pet(null, "Barsik", "cat", LocalDate.now().minusYears(1), "Anna");
        when(petRepository.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));

        assertSame(pet, petService.createPet(pet));
        verify(petRepository).save(pet);
    }

    @Test
    void createPetRejectsBirthdateInFuture() {
        Pet pet = new Pet(null, "Barsik", "cat", LocalDate.now().plusDays(1), "Anna");

        assertThrows(IllegalArgumentException.class, () -> petService.createPet(pet));
        verify(petRepository, never()).save(any());
    }

    @Test
    void createPetRejectsNameWithDigits() {
        Pet pet = new Pet(null, "Barsik2", "cat", LocalDate.now().minusYears(1), "Anna");

        assertThrows(IllegalArgumentException.class, () -> petService.createPet(pet));
    }

    @Test
    void getPetThrowsNotFoundForUnknownId() {
        when(petRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> petService.getPet(42L));
    }

    @Test
    void putPetUpdatesExistingPet() {
        Pet existing = new Pet(1L, "Fufik", "dog", LocalDate.of(2025, 3, 10), "Pasha");
        Pet changes = new Pet(null, "Rex", "dog", LocalDate.of(2025, 3, 10), "Masha");
        when(petRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(petRepository.save(existing)).thenReturn(existing);

        Pet result = petService.putPet(1L, changes);

        assertEquals("Rex", result.getName());
        assertEquals("Masha", result.getOwner());
    }
}
