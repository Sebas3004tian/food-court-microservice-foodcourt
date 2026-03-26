package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class DishNotAvailableException extends RuntimeException {
    public DishNotAvailableException(String message) {
        super(message);
    }
}
