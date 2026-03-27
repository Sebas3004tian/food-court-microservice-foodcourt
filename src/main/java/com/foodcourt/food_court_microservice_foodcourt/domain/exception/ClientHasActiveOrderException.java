package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class ClientHasActiveOrderException extends RuntimeException {
    public ClientHasActiveOrderException(String message) {
        super(message);
    }
}
