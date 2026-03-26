package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PostMapping("/")
    @Operation(summary = "Create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "aaaaaa")//Falta por completar esto
    })
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequestDto createOrderRequestDto){
        orderHandler.createOrder(createOrderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
