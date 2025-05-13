package com.example.demo.pets;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetRepository {
    private final JdbcTemplate jdbcTemplate;

    public PetRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Pet> findAllPets(){
        return jdbcTemplate.query("SELECT * FROM pet", (rs, rowNum) -> new Pet(
                rs.getLong("id"),
                rs.getString("pet_name"),
                rs.getString("pet_type"),
                rs.getDate("birthdate").toLocalDate(),
                rs.getString("pet_owner")));
    }

    public Pet findPetById(Long id) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM pet WHERE id = ?", (rs, rowNum) -> new Pet(
                    rs.getLong("id"),
                    rs.getString("pet_name"),
                    rs.getString("pet_type"),
                    rs.getDate("birthdate").toLocalDate(),
                    rs.getString("pet_owner")), id);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Питомец с таким id не найден");
        }
    }

    public void createNewPet(Pet pet){
        String sql = "INSERT INTO pet (pet_name, pet_type, birthdate, pet_owner) VALUES (?, ?, ?, ?)";
        try{
            jdbcTemplate.update(sql, pet.pet_name(), pet.pet_type(), pet.birthdate(), pet.pet_owner());
        }
        catch(DataAccessException e){
            throw new RuntimeException("Ошибка при сохранении питомца: " + e.getMessage(), e);
        }
    }

    public void changePet(Long id, Pet pet){
        String sql = "UPDATE pet SET pet_name = ?, birthdate = ?, pet_type = ?, pet_owner = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(sql, pet.pet_name(), pet.birthdate(), pet.pet_type(), pet.pet_owner(), id);

        if(updatedRows == 0){
            throw new IllegalArgumentException("Питомец с таким id не найден");
        }
    }

    public void deletePet(Long id) {
        try{
            jdbcTemplate.update("DELETE FROM pet WHERE id = ?", id);
        }
        catch(DataAccessException e){
            throw new RuntimeException("Питомец с таким id не найден");
        }
    }
}
