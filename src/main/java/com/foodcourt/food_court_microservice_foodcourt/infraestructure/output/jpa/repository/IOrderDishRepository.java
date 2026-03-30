package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderDishRepository extends JpaRepository<OrderDishEntity,Long> {
    @Query("""
    SELECT od FROM OrderDishEntity od
    JOIN FETCH od.dish
    WHERE od.order.id IN :orderIds
    """)
    List<OrderDishEntity> findByOrderIdsWithDish(List<Long> orderIds);
}
