package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateEmployeeRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IEmployeeHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IEmployeeRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IEmployeeServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeHandler implements IEmployeeHandler {

    private final IEmployeeServicePort employeeServicePort;

    private final IEmployeeRequestMapper employeeRequestMapper;

    private final IJwtServicePort jwtServicePort;

    @Override
    public void createEmployee(CreateEmployeeRequestDto employeeRequestDto) {
        Long ownerId = jwtServicePort.getAuthenticatedUserId();
        Employee employee = employeeRequestMapper.toEmployee(employeeRequestDto);
        employeeServicePort.createEmployee(ownerId,employee);
    }
}
