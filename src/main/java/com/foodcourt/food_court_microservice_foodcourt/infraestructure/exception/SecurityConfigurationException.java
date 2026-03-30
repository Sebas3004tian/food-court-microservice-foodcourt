package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception;

public class SecurityConfigurationException extends RuntimeException {
    public SecurityConfigurationException(String message) {
        super(message);
    }
}
