package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class DishAlreadyExistsException extends RuntimeException {
    public DishAlreadyExistsException(String message) {
        super(message);
    }
}
