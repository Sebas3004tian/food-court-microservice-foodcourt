package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IEmployeeServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IEmployeePersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.AlreadyExistsException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

public class EmployeeUseCase implements IEmployeeServicePort {

    private final IEmployeePersistencePort employeePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IJwtServicePort jwtServicePort;
    private final IUserExternalPort userExternalPort;

    public EmployeeUseCase(IEmployeePersistencePort employeePersistencePort,
                           IRestaurantPersistencePort restaurantPersistencePort,
                           IJwtServicePort jwtServicePort, IUserExternalPort userExternalPort) {
        this.employeePersistencePort = employeePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.jwtServicePort = jwtServicePort;
        this.userExternalPort = userExternalPort;
    }

    @Override
    public void createEmployee(Employee employee) {

        boolean isEmployee = userExternalPort.isUserEmployee(employee.getUserId());

        if (!isEmployee) {
            throw new InvalidUserRoleException("The user does not exist or does not have the role of EMPLEADO");
        }

        Long ownerId = jwtServicePort.getAuthenticatedUserId();

        Restaurant restaurant = restaurantPersistencePort.findOneById(employee.getRestaurant().getId())
                .orElseThrow(() -> new NoDataFoundException("Not found the Restaurant with id "+employee.getRestaurant().getId()));

        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedException("Your are not the owner of this restaurant");
        }

        boolean exists = employeePersistencePort.existsByUserId(employee.getUserId());

        if (exists) {
            throw new AlreadyExistsException("Employee already exists.");
        }

        employeePersistencePort.createEmployee(employee);
    }
}