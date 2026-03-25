package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class InvalidUserRoleException extends RuntimeException {
    public InvalidUserRoleException(String message) {
        super(message);
    }
}
