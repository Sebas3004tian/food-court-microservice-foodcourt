package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.DishResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;

public interface IDishHandler {
    void createDish(CreateDishRequestDto createDishRequestDto);
    void updateDish(Long dishId, UpdateDishRequestDto updateDishRequestDto);
    void enableOrDisableDish(Long dishId, boolean active);
    PageResponseDto<DishResponseDto> getDishesPagedByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}
