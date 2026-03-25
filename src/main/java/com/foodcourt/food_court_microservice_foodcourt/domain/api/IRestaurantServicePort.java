package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void createRestaurant (Restaurant restaurant);
}
