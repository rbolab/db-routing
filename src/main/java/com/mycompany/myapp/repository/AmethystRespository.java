package com.mycompany.myapp.repository;


import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;



public class AmethystRespository {

    @Inject
    protected JdbcTemplate jdbcTemplate;

}
