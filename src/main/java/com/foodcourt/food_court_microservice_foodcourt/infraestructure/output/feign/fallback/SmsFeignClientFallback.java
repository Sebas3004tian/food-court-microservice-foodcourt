package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.SendSmsRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ISmsFeignClient;
import org.springframework.stereotype.Component;

@Component
public class SmsFeignClientFallback implements ISmsFeignClient {
    @Override
    public String sendSms(SendSmsRequestDto request) {
        return null;
    }
}
