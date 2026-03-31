package com.foodcourt.food_court_microservice_foodcourt.domain.validator;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.DishStatusAlreadySetException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

public class DishValidator {

    private DishValidator() {}

    public static void validateOwnership(Restaurant restaurant, Long userId) {
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new UnauthorizedException();
        }
    }

    public static void validateDishBelongsToRestaurant(Dish dish, Long restaurantId) {
        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("All dishes must belong to the same restaurant");
        }
    }

    public static void validateDishAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public static void validateStatusNotAlreadySet(boolean currentActive, boolean newActive) {
        if (currentActive == newActive) {
            throw new DishStatusAlreadySetException(
                    "Dish is already " + (newActive ? "enabled" : "disabled")
            );
        }
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }
    }
}