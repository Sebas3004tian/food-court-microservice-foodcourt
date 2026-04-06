package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderTraceability;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ITraceabilityFeignClient;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.mapper.ITraceabilityMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TraceabilityFeignAdapter implements ITraceabilityServicePort {

    private final ITraceabilityFeignClient traceabilityFeignClient;

    private final ITraceabilityMapper traceabilityMapper;

    @Override
    public void saveOrderTraceability(OrderTraceability orderTraceability){
        OrderTraceabilityRequestDto orderTraceabilityRequestDto = traceabilityMapper.toDto(orderTraceability);
        traceabilityFeignClient.saveOrderTraceability(orderTraceabilityRequestDto);
    }

    @Override
    public List<OrderEfficiencyResponseDto> getOrderEfficiency(List<Long> orderIds){
        return  traceabilityFeignClient.getOrderEfficiency(orderIds);
    }

    @Override
    public List<EmployeeEfficiencyResponseDto> getEmployeesRanking(List<Long> orderIds){
        return  traceabilityFeignClient.getEmployeesRanking(orderIds);
    }
}