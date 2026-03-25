package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;

public class RestaurantUseCase  implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;


    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void createRestaurant(Restaurant restaurant){
        restaurantPersistencePort.createRestaurant(restaurant);
    }
}
