package com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.SecurityConfigurationException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.security.JwtAutorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final JwtAutorizationFilter jwtAutorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try{
            return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAutorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

        } catch (Exception e) {
            throw new SecurityConfigurationException("Error configuring the security filter chain");
        }
    }


}
