package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;

public interface IDishServicePort {
    void createDish(Dish dish);
}
