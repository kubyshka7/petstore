package com.example.demo.orders;

import com.example.demo.orders.OrderMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;
    private final OrderMapper orderMapper;

    public OrderRepository(JdbcTemplate jdbcTemplate, OrderMapper orderMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.orderMapper = orderMapper;
    }

    public List<Order> findAllOrders(){
        return jdbcTemplate.query("SELECT * FROM order", new BeanPropertyRowMapper<>(Order.class));
    }

    public Order findOrderById(Long id) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM order WHERE order_id = ?", new BeanPropertyRowMapper<>(Order.class), id);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Заказ с таким id не найден");
        }
    }

    public void createNewOrder(Order order){
        String sql = "INSERT INTO order (order_petId, order_customerName, order_orderdate) VALUES (?, ?, ?)";
        try{
            jdbcTemplate.update(sql, order.getPetId(), order.getCustomerName(), order.getOrderDate());
        }
        catch(DataAccessException e){
            throw new RuntimeException("Ошибка при сохранении заказа: " + e.getMessage(), e);
        }
    }

    public void changeOrder(Long id, Status status){
        int updatedRows = jdbcTemplate.update("UPDATE order SET order_status = ? WHERE order_id = ?", status, id);

        if(updatedRows == 0){
            throw new IllegalArgumentException("Заказ с таким id не найден");
        }
    }

    public void deleteOrder(Long id) {
        try{
            jdbcTemplate.update("DELETE FROM order WHERE order_id = ?", id);
        }
        catch(DataAccessException e){
            throw new RuntimeException("Заказ с таким id не найден");
        }
    }
}
