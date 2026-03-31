package com.foodcourt.food_court_microservice_foodcourt.domain.validator;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.AlreadyExistsException;

public class RestaurantValidator {

    private RestaurantValidator() {}

    public static void validateNameNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Restaurant name already exists");
        }
    }

    public static void validateNitNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Restaurant NIT already exists");
        }
    }

    public static void validatePhoneNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Restaurant phone number already exists");
        }
    }

    public static void validateUserIsOwner(boolean isOwner) {
        if (!isOwner) {
            throw new InvalidUserRoleException("The user does not exist or does not have the role of PROPIETARIO");
        }
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }
    }
}