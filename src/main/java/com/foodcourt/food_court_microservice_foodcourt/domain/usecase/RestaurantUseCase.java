package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.AlreadyExistsException;

import java.util.List;

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
            throw new AlreadyExistsException("Restaurant NIT already exists");
        }

        if (restaurantPersistencePort.findOneByPhoneNumber(restaurant.getPhoneNumberRestaurant()).isPresent()) {
            throw new AlreadyExistsException("Restaurant phone number already exists");
        }

        boolean isOwner = userExternalPort.isUserOwner(restaurant.getOwnerId());
        if (!isOwner) {
            throw new InvalidUserRoleException("The user does not exist or does not have the role of PROPIETARIO");
        }

        restaurantPersistencePort.createRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllPagedRestaurants(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }

        return restaurantPersistencePort.findAllPaged(page, size);
    }
}
