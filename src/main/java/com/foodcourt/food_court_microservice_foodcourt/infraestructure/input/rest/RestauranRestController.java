package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateRestaurantRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IRestaurantHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestauranRestController {

    private final IRestaurantHandler restauranHandler;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Void> createOwner(@Valid @RequestBody CreateRestaurantRequestDto createRestaurantRequestDto){
        restauranHandler.createRestaurant(createRestaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
