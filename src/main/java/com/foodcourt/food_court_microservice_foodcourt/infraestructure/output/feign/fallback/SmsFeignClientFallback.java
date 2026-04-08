package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.SendSmsRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ISmsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsFeignClientFallback implements ISmsFeignClient {
    @Override
    public String sendSms(SendSmsRequestDto request) {
        log.error("Error sending SMS to {}. Request: {}", request.getPhoneNumber(), request);
        return null;
    }
}
