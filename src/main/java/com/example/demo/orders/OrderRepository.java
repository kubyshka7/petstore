package com.example.demo.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByPetId(Long petId);

    boolean existsByPetIdAndIdNot(Long petId, Long id);
}
