package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message) {
        super("Please check the pin "+ message +", because its not the correct");
    }
}
