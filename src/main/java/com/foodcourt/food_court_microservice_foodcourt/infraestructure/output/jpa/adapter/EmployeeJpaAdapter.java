package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IEmployeePersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.EmployeeEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IEmployeeEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IEmployeeRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeJpaAdapter implements IEmployeePersistencePort {

    private final IEmployeeRepository employeeRepository;

    private final IEmployeeEntityMapper employeeEntityMapper;

    @Override
    public Employee createEmployee(Employee employee) {
        EmployeeEntity employeeEntity = employeeRepository.save(employeeEntityMapper.toEntity(employee));
        return employeeEntityMapper.toEmployee(employeeEntity);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return employeeRepository.existsByUserId(userId);
    }

    @Override
    public Optional<Employee> findOneByUserId(Long userId) {
        return employeeRepository.findByUserId(userId)
                .map(employeeEntityMapper::toEmployee);
    }
}
