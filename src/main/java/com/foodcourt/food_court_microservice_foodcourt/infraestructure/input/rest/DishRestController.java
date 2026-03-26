package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.DishResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.RestaurantResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @PostMapping("/")
    @Operation(summary = "Create an dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Dish already exists")
    })
    public ResponseEntity<Void> createDish(@Valid @RequestBody CreateDishRequestDto createDishRequestDto){
        dishHandler.createDish(createDishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @PutMapping("/{dishId}")
    @Operation(summary = "Update an dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public ResponseEntity<Void> updateDish(@PathVariable Long dishId, @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto){
        dishHandler.updateDish(dishId, updateDishRequestDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @PatchMapping("/{dishId}/active")
    @Operation(summary = "Enable or disable  dish status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public ResponseEntity<Void> enableOrDisableDish(@PathVariable Long dishId, @RequestParam boolean active) {
        dishHandler.enableOrDisableDish(dishId, active);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get all available dishes of a restaurant ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes of a Restaurant paged"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "There are no dishes created")
    })
    public ResponseEntity<List<DishResponseDto>> getAllPagedRestaurants(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(dishHandler.getDishesPagedByRestaurant(restaurantId,categoryId, page, size));
    }

}
