package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ExternalServiceException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ITraceabilityFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraceabilityFeignClientFallback implements ITraceabilityFeignClient {
    @Override
    public void saveOrderTraceability(OrderTraceabilityRequestDto orderTraceabilityRequestDto) {
        throw new ExternalServiceException("cant save traceability");
    }

    @Override
    public List<OrderEfficiencyResponseDto> getOrderEfficiency(List<Long> orderIds) {
        throw new ExternalServiceException("cant get Order Efficiency");
    }

    @Override
    public List<EmployeeEfficiencyResponseDto> getEmployeesRanking(List<Long> orderIds) {
        throw new ExternalServiceException("cant get Employees Ranking");
    }
}
