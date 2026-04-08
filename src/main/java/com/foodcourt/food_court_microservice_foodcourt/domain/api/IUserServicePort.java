package com.foodcourt.food_court_microservice_foodcourt.domain.api;

public interface IUserServicePort {
    boolean isUserOwner(Long userId);
    boolean isUserEmployee(Long userId);
    String getPhone(Long id);
    String getEmail(Long id);
}
