package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class InvalidUserRoleException extends RuntimeException {
    public InvalidUserRoleException(String role) {
        super("The user does not exist or does not have the role of " + role);
    }
}
