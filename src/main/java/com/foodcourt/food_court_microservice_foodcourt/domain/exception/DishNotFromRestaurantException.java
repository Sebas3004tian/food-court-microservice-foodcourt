package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class DishNotFromRestaurantException extends RuntimeException {
    public DishNotFromRestaurantException(String message) {
        super(message);
    }
}
