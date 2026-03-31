package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class ClientHasActiveOrderException extends RuntimeException {
    public ClientHasActiveOrderException() {
        super("Client cannot create a new order while having an active order");
    }
}
