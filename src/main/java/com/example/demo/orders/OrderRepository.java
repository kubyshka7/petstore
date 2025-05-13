package com.example.demo.orders;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Order> findAllOrders(){
        return jdbcTemplate.query("SELECT * FROM order_", (rs, rowNum) -> new Order(
                rs.getLong("id"),
                rs.getLong("pet_id"),
                rs.getString("customer_name"),
                rs.getDate("order_date").toLocalDate(),
                Status.valueOf(rs.getString("status"))));
    }

    public Order findOrderById(Long id) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM order_ WHERE id = ?", (rs, rowNum) -> new Order(
                    rs.getLong("id"),
                    rs.getLong("pet_id"),
                    rs.getString("customer_name"),
                    rs.getDate("order_date").toLocalDate(),
                    Status.valueOf(rs.getString("status"))), id);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Заказ с таким id не найден");
        }
    }

    public void createNewOrder(Order order){
        String sql = "INSERT INTO order_ (pet_id, customer_name, order_date, status) VALUES (?, ?, ?, ?)";
        try{
            jdbcTemplate.update(sql, order.petId(), order.customerName(), order.orderDate(), "CREATED");
        }
        catch(DataAccessException e){
            throw new RuntimeException("Ошибка при сохранении заказа: " + e.getMessage(), e);
        }
    }

    public void changeOrder(Long id, OrderRequest request){
        String sql = "UPDATE order_ SET pet_id = ?, customer_name = ?, order_date = ?, status = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(sql, request.petId(), request.customerName(), request.orderDate(), request.status().toString(), id);

        if(updatedRows == 0){
            throw new IllegalArgumentException("Заказ с таким id не найден");
        }
    }

    public void deleteOrder(Long id) {
        try{
            jdbcTemplate.update("DELETE FROM order_ WHERE id = ?", id);
        }
        catch(DataAccessException e){
            throw new RuntimeException("Заказ с таким id не найден");
        }
    }

    public Order findOrderByPetId(Long petId) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM order_ WHERE pet_id = ?", (rs, rowNum) -> new Order(
                    rs.getLong("id"),
                    rs.getLong("pet_id"),
                    rs.getString("customer_name"),
                    rs.getDate("order_date").toLocalDate(),
                    Status.valueOf(rs.getString("status"))), petId);
        }
        catch (DataAccessException e){
            return null;
        }
    }
}
