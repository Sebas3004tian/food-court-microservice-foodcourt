package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDishRepository extends JpaRepository<OrderDishEntity,Long> {
}
