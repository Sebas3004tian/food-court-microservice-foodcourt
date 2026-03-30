package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void createRestaurant (Restaurant restaurant);
    List<Restaurant> getAllPagedRestaurants(int page, int size);
}
