package com.foodcourt.food_court_microservice_foodcourt.domain.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super("Not found Employee "+message);
    }
}