package com.example.demo.pets;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @SequenceGenerator(
            name = "pet_sequence",
            sequenceName = "pet_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pet_sequence"
    )
    private Long id;
    private String name;
    private String type;
    private LocalDate birthdate;
    private String owner;

    public Pet() {
    }

    public Pet(Long id, String name, String type, LocalDate birthdate, String owner) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.birthdate = birthdate;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
