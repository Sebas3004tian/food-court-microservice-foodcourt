package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateEmployeeRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeRequestMapper {

    @Mapping(source = "restaurantId", target = "restaurant.id")
    Employee toEmployee(CreateEmployeeRequestDto createEmployeeRequestDto);
}
