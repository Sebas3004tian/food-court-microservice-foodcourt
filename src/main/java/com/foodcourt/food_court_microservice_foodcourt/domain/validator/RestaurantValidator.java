package com.foodcourt.food_court_microservice_foodcourt.domain.validator;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.AlreadyExistsException;

public class RestaurantValidator {

    private RestaurantValidator() {
    }

    public static void validateNameNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Restaurant name");
        }
    }

    public static void validateNitNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Restaurant NIT");
        }
    }

    public static void validatePhoneNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Restaurant phone number");
        }
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }
    }

    public static void validateOwnerAlreadyHaveRestaurant(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("The owner user already have a restaurant");
        }
    }
}