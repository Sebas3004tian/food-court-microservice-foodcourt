package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;

import java.util.List;

public interface ITraceabilityHandler {
    List<OrderEfficiencyResponseDto> getOrderEfficiency();

    List<EmployeeEfficiencyResponseDto> getEmployeeRanking();
}
