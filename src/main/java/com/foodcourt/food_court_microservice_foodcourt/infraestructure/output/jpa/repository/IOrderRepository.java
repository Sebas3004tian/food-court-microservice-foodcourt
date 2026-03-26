package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

}
