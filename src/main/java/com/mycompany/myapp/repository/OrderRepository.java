package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "amethystCache", keyGenerator = "amethystKeyGenerator")
public class OrderRepository extends AmethystRespository {

    @Cacheable
    public List<Order> findAll(){
        List<Order> orders = jdbcTemplate.query("select id, date from orders", new BeanPropertyRowMapper(Order.class));
        return orders;
    }

}
