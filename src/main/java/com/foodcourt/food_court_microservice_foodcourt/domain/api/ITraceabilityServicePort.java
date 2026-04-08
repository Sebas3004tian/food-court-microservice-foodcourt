package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderTraceability;

import java.util.List;

public interface ITraceabilityServicePort {
    void saveOrderTraceability(OrderTraceability orderTraceability);

    List<OrderEfficiencyResponseDto> getOrderEfficiency(List<Long> orderIds);

    List<EmployeeEfficiencyResponseDto> getEmployeesRanking(List<Long> orderIds);
}
