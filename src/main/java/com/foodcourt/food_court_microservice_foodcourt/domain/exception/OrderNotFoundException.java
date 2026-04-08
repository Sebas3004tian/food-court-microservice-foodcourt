package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super("Not found Order "+message);
    }
}