package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception;

public class SecurityConfigurationException extends RuntimeException {
    public SecurityConfigurationException() {
        super("Error configuring the security filter chain");
    }
}
