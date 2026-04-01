package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ExternalServiceException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.IUserFeignClient;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFallback implements IUserFeignClient {

    @Override
    public String getUserRole(Long id) {
        throw new ExternalServiceException("User service unavailable");
    }

    @Override
    public String getPhone(Long id) {
        return null;
    }

    @Override
    public String getEmail(Long id) {
        return null;
    }
}
