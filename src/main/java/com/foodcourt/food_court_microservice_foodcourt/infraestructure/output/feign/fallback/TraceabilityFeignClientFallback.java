package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ExternalServiceException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ITraceabilityFeignClient;
import org.springframework.stereotype.Component;

@Component
public class TraceabilityFeignClientFallback implements ITraceabilityFeignClient {
    @Override
    public void saveOrderTraceability(OrderTraceabilityRequestDto orderTraceabilityRequestDto) {
        throw new ExternalServiceException("Traceability service unavailable");
    }
}
