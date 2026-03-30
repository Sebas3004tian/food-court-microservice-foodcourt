package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.SendSmsRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration.FeignClientConfiguration;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback.SmsFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "message-service",
        url = "${message-service.url}",
        configuration = FeignClientConfiguration.class,
       fallback = SmsFeignClientFallback.class
)
public interface ISmsFeignClient {
    @PostMapping("/sms/send")
    String sendSms(@RequestBody SendSmsRequestDto request);
}
