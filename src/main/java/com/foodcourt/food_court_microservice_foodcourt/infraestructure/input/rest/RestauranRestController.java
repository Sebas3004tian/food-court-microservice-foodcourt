package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.handler.IRestauranHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestauranRestController {

    private final IRestauranHandler restauranHandler;

}
