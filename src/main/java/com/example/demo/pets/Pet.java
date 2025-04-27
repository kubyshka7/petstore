package com.example.demo.pets;

import java.time.LocalDate;

// todo почитать про record в java
public class Pet {
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
}
