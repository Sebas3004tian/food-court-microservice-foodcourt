package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderTraceability;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ITraceabilityFeignClient;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.mapper.ITraceabilityMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TraceabilityFeignAdapter implements ITraceabilityServicePort {

    private final ITraceabilityFeignClient traceabilityFeignClient;

    private final ITraceabilityMapper traceabilityMapper;

    @Override
    public void saveOrderTraceability(OrderTraceability orderTraceability){
        OrderTraceabilityRequestDto orderTraceabilityRequestDto = traceabilityMapper.toDto(orderTraceability);
        traceabilityFeignClient.saveOrderTraceability(orderTraceabilityRequestDto);
    }
}