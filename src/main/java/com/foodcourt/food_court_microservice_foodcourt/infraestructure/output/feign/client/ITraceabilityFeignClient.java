package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration.FeignClientConfiguration;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback.TraceabilityFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "traceability-service",
        url = "${traceability-service.url}",
        configuration = FeignClientConfiguration.class,
        fallback = TraceabilityFeignClientFallback.class
)
public interface ITraceabilityFeignClient {
    @PostMapping("/traceability/")
    void saveOrderTraceability(@RequestBody OrderTraceabilityRequestDto orderTraceabilityRequestDto);
}
