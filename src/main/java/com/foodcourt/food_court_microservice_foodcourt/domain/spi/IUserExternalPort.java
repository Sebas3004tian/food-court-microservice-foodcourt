package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

public interface IUserExternalPort {
    boolean isUserOwner(Long userId);
    boolean isUserEmployee(Long userId);
    String getPhone(Long id);
}
