package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exceptionhandler;

public enum ExceptionResponse {

    VALIDATION_ERROR("Validation error"),
    ACCESS_DENIED("You do not have permission to access this resource"),
    SECURITY_CONFIGURATION_ERROR("Error configuring security");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}