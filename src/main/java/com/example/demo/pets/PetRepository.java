package com.example.demo.pets;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Repository
public class PetRepository {
    private final JdbcTemplate jdbcTemplate;
    private final PetMapper petMapper;

    public PetRepository(JdbcTemplate jdbcTemplate, PetMapper petMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.petMapper = petMapper;
    }

    public List<Pet> findAllPets(){
        return jdbcTemplate.query("SELECT * FROM pet", new BeanPropertyRowMapper<>(Pet.class));
    }

    public Pet findPetById(Long id) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM pet WHERE pet_id = ?", new BeanPropertyRowMapper<>(Pet.class), id);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Питомец с таким id не найден");
        }
    }

    public void createNewPet(Pet pet){
        String sql = "INSERT INTO pet (pet_name, pet_type, pet_birthdate, pet_owner) VALUES (?, ?, ?, ?)";
        try{
            jdbcTemplate.update(sql, pet.getName(), pet.getType(), pet.getBirthdate(), pet.getOwner());
        }
        catch(DataAccessException e){
            throw new RuntimeException("Ошибка при сохранении питомца: " + e.getMessage(), e);
        }
    }

    public void changePet(Long id, Pet pet){
        String sql = "UPDATE pet SET pet_name = ?, pet_birthdate = ?, pet_type = ?, pet_owner = ? WHERE pet_id = ?";
        int updatedRows = jdbcTemplate.update(sql, pet.getName(), pet.getBirthdate(), pet.getType(), pet.getOwner(), id);

        if(updatedRows == 0){
            throw new IllegalArgumentException("Питомец с таким id не найден");
        }
    }

    public void deletePet(Long id) {
        try{
            jdbcTemplate.update("DELETE FROM pet WHERE pet_id = ?", id);
        }
        catch(DataAccessException e){
            throw new RuntimeException("Питомец с таким id не найден");
        }
    }
}
