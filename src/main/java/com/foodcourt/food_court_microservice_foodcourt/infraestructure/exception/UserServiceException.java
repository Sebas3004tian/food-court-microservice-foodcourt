package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception;

public class UserServiceException extends RuntimeException {
    private final int status;

    public UserServiceException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
