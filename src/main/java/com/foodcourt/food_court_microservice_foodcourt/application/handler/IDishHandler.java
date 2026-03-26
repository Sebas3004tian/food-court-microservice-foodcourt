package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;

public interface IDishHandler {
    void createDish(CreateDishRequestDto createDishRequestDto);
}
