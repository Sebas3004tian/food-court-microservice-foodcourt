package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class DishStatusAlreadySetException extends RuntimeException {
    public DishStatusAlreadySetException(String message) {
        super(message);
    }
}
