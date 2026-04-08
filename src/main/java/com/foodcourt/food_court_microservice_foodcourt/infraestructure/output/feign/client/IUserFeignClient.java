package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration.FeignClientConfiguration;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.fallback.UserFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}",
        configuration = FeignClientConfiguration.class,
        fallback = UserFeignClientFallback.class
)
public interface IUserFeignClient {

    @GetMapping("/user/{id}/role")
    String getUserRole(@PathVariable Long id);

    @GetMapping("/user/{id}/phone")
    String getPhone(@PathVariable Long id);

    @GetMapping("/user/{id}/email")
    String getEmail(@PathVariable Long id);
}