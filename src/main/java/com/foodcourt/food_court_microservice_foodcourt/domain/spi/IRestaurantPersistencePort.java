package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;

public interface IRestaurantPersistencePort {
    Restaurant createRestaurant(Restaurant restaurant);
}
