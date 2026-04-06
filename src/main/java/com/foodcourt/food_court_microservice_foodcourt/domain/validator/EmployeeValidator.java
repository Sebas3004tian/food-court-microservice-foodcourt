package com.foodcourt.food_court_microservice_foodcourt.domain.validator;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.AlreadyExistsException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.UserRole;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

public class EmployeeValidator {

    private EmployeeValidator() {}

    public static void validateUserIsEmployee(boolean isEmployee) {
        if (!isEmployee) {
            throw new InvalidUserRoleException(UserRole.EMPLEADO.name());
        }
    }

    public static void validateOwnership(Restaurant restaurant, Long ownerId) {
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedException();
        }
    }

    public static void validateEmployeeNotExists(boolean exists) {
        if (exists) {
            throw new AlreadyExistsException("Employee");
        }
    }
}