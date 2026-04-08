package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {

        super(message + "already exist.");
    }
}
