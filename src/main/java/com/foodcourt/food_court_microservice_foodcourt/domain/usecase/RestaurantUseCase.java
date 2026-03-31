package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;
import com.foodcourt.food_court_microservice_foodcourt.domain.validator.RestaurantValidator;

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

        RestaurantValidator.validateNameNotExists(
                restaurantPersistencePort.findOneByName(restaurant.getName()).isPresent()
        );

        RestaurantValidator.validateNitNotExists(
                restaurantPersistencePort.findOneByNit(restaurant.getNit()).isPresent()
        );

        RestaurantValidator.validatePhoneNotExists(
                restaurantPersistencePort.findOneByPhoneNumber(restaurant.getPhoneNumberRestaurant()).isPresent()
        );

        RestaurantValidator.validateUserIsOwner(userExternalPort.isUserOwner(restaurant.getOwnerId()));

        restaurantPersistencePort.createRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllPagedRestaurants(int page, int size) {
        RestaurantValidator.validatePaginationParams(page, size);

        return restaurantPersistencePort.findAllPaged(page, size);
    }
}
