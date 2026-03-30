package com.foodcourt.food_court_microservice_foodcourt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FoodCourtMicroserviceFoodcourtApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCourtMicroserviceFoodcourtApplication.class, args);
	}

}
