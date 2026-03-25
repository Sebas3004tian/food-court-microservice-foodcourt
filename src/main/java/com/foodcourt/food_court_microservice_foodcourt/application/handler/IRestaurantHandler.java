package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateRestaurantRequestDto;

public interface IRestaurantHandler {
    void createRestaurant(CreateRestaurantRequestDto createRestaurantRequestDto);
}
