package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IDishHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @PostMapping("/")
    public ResponseEntity<Void> createDish(@Valid @RequestBody CreateDishRequestDto createDishRequestDto){
        dishHandler.createDish(createDishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @PutMapping("/{dishId}")
    public ResponseEntity<Void> updateDish(@PathVariable Long dishId, @Valid @RequestBody UpdateDishRequestDto updateDishRequestDto){
        dishHandler.updateDish(dishId, updateDishRequestDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @PatchMapping("/{dishId}/active")
    public ResponseEntity<Void> enableOrDisableDish(@PathVariable Long dishId, @RequestParam boolean active) {
        dishHandler.enableOrDisableDish(dishId, active);
        return ResponseEntity.ok().build();
    }


}
