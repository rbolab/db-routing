package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
public class OrderRepository {

    @Inject
    private JdbcTemplate jdbcTemplate;

    public List<Order> findAll(){
        List<Order> orders = jdbcTemplate.query("select id, date from orders", new BeanPropertyRowMapper(Order.class));
        return orders;
    }

}
