package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.UserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;

public class RestaurantUseCase  implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserExternalPort userExternalPort;


    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserExternalPort userExternalPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userExternalPort = userExternalPort;
    }

    @Override
    public void createRestaurant(Restaurant restaurant){

        boolean isOwner = userExternalPort.isUserOwner(restaurant.getOwnerId());

        if (!isOwner) {
            throw new UserRoleException("The user does not exist or does not have the role of OWNER");
        }

        restaurantPersistencePort.createRestaurant(restaurant);
    }
}
