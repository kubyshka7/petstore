package com.example.demo.orders;

import com.example.demo.common.NotFoundException;
import com.example.demo.pets.Pet;
import com.example.demo.pets.PetRepository;
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
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    PetRepository petRepository;

    @InjectMocks
    OrderService orderService;

    private final Pet pet = new Pet(1L, "Masik", "cat", LocalDate.of(2024, 8, 10), "Sasha");

    @Test
    void createOrderForFreePet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(orderRepository.existsByPetId(1L)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order order = orderService.createOrder(new OrderRequest(1L, "Ivan", LocalDate.now(), "new"));

        assertSame(pet, order.getPet());
        assertEquals("Ivan", order.getCustomerName());
    }

    @Test
    void createOrderRejectsPetThatAlreadyHasOrder() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(orderRepository.existsByPetId(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(new OrderRequest(1L, "Ivan", LocalDate.now(), "new")));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrderRejectsUnknownPet() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.createOrder(new OrderRequest(99L, "Ivan", LocalDate.now(), "new")));
    }

    @Test
    void createOrderRejectsDateInFuture() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(new OrderRequest(1L, "Ivan", LocalDate.now().plusDays(1), "new")));
    }

    @Test
    void deleteOrderChecksOrdersNotPets() {
        when(orderRepository.existsById(5L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.deleteOrder(5L));
        verifyNoInteractions(petRepository);
    }
}
