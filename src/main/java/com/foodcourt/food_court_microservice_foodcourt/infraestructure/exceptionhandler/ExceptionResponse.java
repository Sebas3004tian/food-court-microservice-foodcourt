package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exceptionhandler;

public enum ExceptionResponse {

    VALIDATION_ERROR("Validation error"),
    ACCESS_DENIED("You do not have permission to access this resource"),
    SECURITY_CONFIGURATION_ERROR("Error configuring security"),
    USER_ROLE_EROR("Error with the user"),
    RESTAURANT_ALREADY_EXISTS("Restaurant already exists"),
    RESTAURANT_NOT_FOUND("Restaurant not found"),
    CATEGORY_NOT_FOUND("Restaurant not found"),
    UNAUTHORIZED_ERROR("You are not autorized to do this action"),
    USER_MICROSERVICE_ERROR("Error with user or user role"),
    DISH_ALREADY_EXISTS("Dish already exists");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}