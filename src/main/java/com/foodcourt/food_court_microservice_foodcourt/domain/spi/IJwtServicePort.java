package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import io.jsonwebtoken.Claims;

public interface IJwtServicePort {
    Long getAuthenticatedUserId();

    Claims extractClaims(String token);
}
