package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String message) {
        super("Not found the Dish with id "+message);
    }
}
