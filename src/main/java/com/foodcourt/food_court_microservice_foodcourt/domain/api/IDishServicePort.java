package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IDishServicePort {
    void createDish(Long userId, Dish dish);
    void updateDish(Long userId, Long dishId, BigDecimal dishPrice, String dishDescription);
    void enableOrDisableDish(Long userId, Long dishId, boolean active);
    Page<Dish> getDishesPagedByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}
