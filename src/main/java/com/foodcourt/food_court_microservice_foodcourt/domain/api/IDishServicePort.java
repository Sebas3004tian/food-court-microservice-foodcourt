package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;

import java.math.BigDecimal;

public interface IDishServicePort {
    void createDish(Dish dish);
    void updateDish(Long dishId, BigDecimal dishPrice, String dishDescription);
}
