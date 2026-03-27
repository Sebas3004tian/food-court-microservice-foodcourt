package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;

public interface IEmployeeServicePort {
    void createEmployee(Employee employee);
}
