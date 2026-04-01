package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.RestaurantNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.validator.RestaurantValidator;
import org.springframework.data.domain.Page;


public class RestaurantUseCase  implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserServicePort userServicePort;


    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserServicePort userServicePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userServicePort = userServicePort;
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

        RestaurantValidator.validateUserIsOwner(userServicePort.isUserOwner(restaurant.getOwnerId()));

        RestaurantValidator.validateOwnerAlreadyHaveRestaurant(
                restaurantPersistencePort.findRestaurantId(restaurant.getOwnerId()).isPresent()
        );

        restaurantPersistencePort.createRestaurant(restaurant);
    }

    @Override
    public Page<Restaurant> getAllPagedRestaurants(int page, int size) {
        RestaurantValidator.validatePaginationParams(page, size);

        return restaurantPersistencePort.findAllPaged(page, size);
    }

    @Override
    public Long getRestaurantId(Long authenticatedUserId) {
        return restaurantPersistencePort.findRestaurantId(authenticatedUserId)
                .orElseThrow(() -> new RestaurantNotFoundException(""));
    }
}