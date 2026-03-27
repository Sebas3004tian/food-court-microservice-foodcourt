package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateEmployeeRequestDto;

public interface IEmployeeHandler {
    void createEmployee(CreateEmployeeRequestDto employeeRequestDto);
}
