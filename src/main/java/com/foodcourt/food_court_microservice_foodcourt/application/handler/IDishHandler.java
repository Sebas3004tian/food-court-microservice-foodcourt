package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.UpdateDishRequestDto;

public interface IDishHandler {
    void createDish(CreateDishRequestDto createDishRequestDto);
    void updateDish(Long dishId, UpdateDishRequestDto updateDishRequestDto);
    void enableOrDisableDish(Long dishId, boolean active);
}
