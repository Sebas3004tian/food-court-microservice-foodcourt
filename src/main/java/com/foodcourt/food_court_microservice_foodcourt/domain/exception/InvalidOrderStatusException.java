package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String message) {
        super("Invalid order status: +  "+message);
    }
}
