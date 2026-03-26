package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
