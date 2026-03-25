package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
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

        if (restaurantPersistencePort.findOneByNit(restaurant.getNit()).isPresent()) {
            throw new RestaurantAlreadyExistsException("Restaurant NIT already exists");
        }

        if (restaurantPersistencePort.findOneByPhoneNumber(restaurant.getPhoneNumberRestaurant()).isPresent()) {
            throw new RestaurantAlreadyExistsException("Restaurant phone number already exists");
        }

        boolean isOwner = userExternalPort.isUserOwner(restaurant.getOwnerId());

        if (!isOwner) {
            throw new InvalidUserRoleException("The user does not exist or does not have the role of PROPIETARIO");
        }

        restaurantPersistencePort.createRestaurant(restaurant);
    }
}
