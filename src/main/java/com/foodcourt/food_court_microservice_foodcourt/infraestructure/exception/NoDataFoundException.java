package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String message) {
        super(message);
    }
}
