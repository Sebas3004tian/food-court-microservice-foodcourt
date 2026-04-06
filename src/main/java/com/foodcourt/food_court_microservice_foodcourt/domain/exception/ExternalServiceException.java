package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message + "Traceability service unavailable");
    }
}
