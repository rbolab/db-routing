package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Fund;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fund entity.
 */
@SuppressWarnings("unused")
public interface FundRepository extends JpaRepository<Fund,Long> {

}
