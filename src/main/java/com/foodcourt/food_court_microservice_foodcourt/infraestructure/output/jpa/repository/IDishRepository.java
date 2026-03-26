package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Optional<DishEntity> findOneByName(String name);
}
