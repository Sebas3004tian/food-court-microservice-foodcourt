package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
