package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super("Not found Category "+message);
    }
}