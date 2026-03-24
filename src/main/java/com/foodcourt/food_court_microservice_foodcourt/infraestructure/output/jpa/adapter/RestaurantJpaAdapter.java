package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter {

    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;
}
