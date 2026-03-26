package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByName(String name);

}
