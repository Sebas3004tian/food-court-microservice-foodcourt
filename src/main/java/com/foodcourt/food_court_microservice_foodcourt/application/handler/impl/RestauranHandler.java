package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.handler.IRestauranHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IRestaurantRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestauranHandler implements IRestauranHandler {

    private final IRestaurantServicePort restaurantServicePort;

    private final IRestaurantRequestMapper restaurantRequestMapper;
}
