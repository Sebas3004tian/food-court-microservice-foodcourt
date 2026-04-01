package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    void createRestaurant (Restaurant restaurant);
    Page<Restaurant> getAllPagedRestaurants(int page, int size);
    Long getRestaurantId(Long authenticatedUserId);
}
