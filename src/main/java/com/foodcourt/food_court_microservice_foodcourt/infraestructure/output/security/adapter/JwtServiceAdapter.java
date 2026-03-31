package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.security.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtServiceAdapter implements IJwtServicePort {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(auth.getName());
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
