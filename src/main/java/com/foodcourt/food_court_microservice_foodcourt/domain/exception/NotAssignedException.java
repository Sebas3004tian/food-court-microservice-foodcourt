package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class NotAssignedException extends RuntimeException {
    public NotAssignedException() {
        super("You are not assigned to this order");
    }
}
