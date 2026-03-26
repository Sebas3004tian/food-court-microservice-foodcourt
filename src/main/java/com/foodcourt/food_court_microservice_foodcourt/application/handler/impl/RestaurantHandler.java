package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateRestaurantRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.RestaurantResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IRestaurantHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IRestaurantRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IRestaurantResponseMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;

    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @Override
    public void createRestaurant(CreateRestaurantRequestDto restaurantRequestDto){
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantServicePort.createRestaurant(restaurant);

    }

    @Override
    public List<RestaurantResponseDto> getAllPagedRestaurants(int page, int size) {
        return restaurantResponseMapper.toResponseList(restaurantServicePort.getAllPagedRestaurants(page,size));
    }

}
