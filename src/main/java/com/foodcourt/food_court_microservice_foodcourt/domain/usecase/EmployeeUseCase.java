package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IEmployeeServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IEmployeePersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;
import com.foodcourt.food_court_microservice_foodcourt.domain.validator.EmployeeValidator;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.RestaurantNotFoundException;

public class EmployeeUseCase implements IEmployeeServicePort {

    private final IEmployeePersistencePort employeePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserExternalPort userExternalPort;

    public EmployeeUseCase(IEmployeePersistencePort employeePersistencePort,
                           IRestaurantPersistencePort restaurantPersistencePort, IUserExternalPort userExternalPort) {
        this.employeePersistencePort = employeePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userExternalPort = userExternalPort;
    }

    @Override
    public void createEmployee(Long ownerId,Employee employee) {

        boolean isEmployee = userExternalPort.isUserEmployee(employee.getUserId());

        if (!isEmployee) {
            throw new InvalidUserRoleException("The user does not exist or does not have the role of EMPLEADO");
        }

        Restaurant restaurant = restaurantPersistencePort.findOneById(employee.getRestaurant().getId())
                .orElseThrow(() -> new RestaurantNotFoundException(employee.getRestaurant().getId().toString()));

        EmployeeValidator.validateOwnership(restaurant, ownerId);
        EmployeeValidator.validateEmployeeNotExists(employeePersistencePort.existsByUserId(employee.getUserId()));

        employeePersistencePort.createEmployee(employee);
    }
}