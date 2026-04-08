package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration.FeignClientConfiguration;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback.TraceabilityFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "traceability-service",
        url = "${traceability-service.url}",
        configuration = FeignClientConfiguration.class,
        fallback = TraceabilityFeignClientFallback.class
)
public interface ITraceabilityFeignClient {
    @PostMapping("/traceability/")
    void saveOrderTraceability(@RequestBody OrderTraceabilityRequestDto orderTraceabilityRequestDto);


    @GetMapping("/traceability/efficiency/orders")
    List<OrderEfficiencyResponseDto> getOrderEfficiency(@RequestParam List<Long> orderIds);


    @GetMapping("/traceability/efficiency/employees")
    List<EmployeeEfficiencyResponseDto> getEmployeesRanking(@RequestParam List<Long> orderIds);
}
