package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;

public interface IEmployeePersistencePort {

    Employee createEmployee(Employee employee);

    boolean existsByUserId(Long userId);
}
