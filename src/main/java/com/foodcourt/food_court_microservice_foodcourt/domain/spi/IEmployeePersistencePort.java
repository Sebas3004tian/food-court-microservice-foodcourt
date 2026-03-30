package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;

import java.util.Optional;

public interface IEmployeePersistencePort {

    Employee createEmployee(Employee employee);

    boolean existsByUserId(Long userId);
    Optional<Employee> findOneByUserId(Long userId);
}
