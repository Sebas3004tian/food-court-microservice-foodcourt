package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateRestaurantRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.RestaurantResponseDto;

import java.util.List;

public interface IRestaurantHandler {
    void createRestaurant(CreateRestaurantRequestDto createRestaurantRequestDto);
    List<RestaurantResponseDto> getAllPagedRestaurants(int page, int size);
}
