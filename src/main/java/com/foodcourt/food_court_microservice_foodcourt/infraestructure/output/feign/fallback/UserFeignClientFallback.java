package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ExternalServiceException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.IUserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class UserFeignClientFallback implements IUserFeignClient {

    @Override
    public String getUserRole(Long id) {
        throw new ExternalServiceException("User not found or User service unavailable");
    }

    @Override
    public String getPhone(Long id) {
        log.error("Error getting phone to User Service");
        return null;
    }

    @Override
    public String getEmail(Long id) {
        log.error("Error getting email to User Service");
        return null;
    }
}
