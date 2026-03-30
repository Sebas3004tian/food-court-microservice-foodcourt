package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.security.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtServiceAdapter implements IJwtServicePort {

    @Override
    public Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(auth.getName());
    }
}
