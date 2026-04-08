package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.ITraceabilityHandler;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TraceabilityHandler implements ITraceabilityHandler {


    private final ITraceabilityServicePort traceabilityServicePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IOrderServicePort orderServicePort;

    private final IJwtServicePort jwtServicePort;

    public List<Long> getOrdersId() {
        Long ownerId = jwtServicePort.getAuthenticatedUserId();
        Long restaurantId = restaurantServicePort.getRestaurantId(ownerId);
        return orderServicePort.getOrdersIdsByRestaurantId(restaurantId);
    }

    @Override
    public List<OrderEfficiencyResponseDto> getOrderEfficiency() {
        return traceabilityServicePort.getOrderEfficiency(getOrdersId());
    }

    @Override
    public List<EmployeeEfficiencyResponseDto> getEmployeeRanking() {
        return traceabilityServicePort.getEmployeesRanking(getOrdersId());
    }
}
