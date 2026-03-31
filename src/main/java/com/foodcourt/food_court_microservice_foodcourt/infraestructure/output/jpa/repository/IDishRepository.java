package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    Optional<DishEntity> findOneByName(String name);

    @Query("""
    SELECT d FROM DishEntity d
    WHERE d.restaurant.id = :restaurantId
    AND d.active = true
    """)
    Page<DishEntity> findByRestaurant(Long restaurantId, Pageable pageable);

    @Query("""
    SELECT d FROM DishEntity d
    WHERE d.restaurant.id = :restaurantId
    AND d.category.id = :categoryId
    AND d.active = true
    """)
    Page<DishEntity> findByRestaurantAndCategoryPaged(Long restaurantId, Long categoryId, Pageable pageable);
}
