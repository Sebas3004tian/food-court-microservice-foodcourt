package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exceptionhandler;

public enum ExceptionResponse {

    ACCESS_DENIED("You do not have permission to access this resource"),
    UNAUTHORIZED_ERROR("You are not autorized to do this action"),
    VALIDATION_ERROR("Validation error"),
    SECURITY_CONFIGURATION_ERROR("Error configuring security"),
    USER_ROLE_ERROR("Error with the user"),
    DATA_NOT_FOUND("Data not found"),
    ALREADY_EXISTS("Already exists"),
    USER_MICROSERVICE_ERROR("Error with user or user role"),
    DISH_STATUS_ALREADY_SET("Dish status already set"),
    ILLEGAL_ARGUMENT_ERROR("Error in one argument"),
    INVALID_ORDER_STATUS_ERROR("Error with the order status"),
    CLIENT_HAS_ACTIVE_ORDER("The customer already has an active order");


    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}