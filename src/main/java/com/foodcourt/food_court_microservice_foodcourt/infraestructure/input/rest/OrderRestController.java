package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IOrderHandler;
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
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/")
    @Operation(summary = "Create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Conflict with some attribute")
    })
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequestDto createOrderRequestDto){
        orderHandler.createOrder(createOrderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders paged with determinate status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders of a status paged"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "There are no orders created")
    })
    public ResponseEntity<PageResponseDto<OrderResponseDto>> getOrderPagedByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(orderHandler.getOrderPagedByStatus(status,page, size));
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{orderId}/assign")
    @Operation(summary = "Assign an Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Conflicts with assign the order")
    })
    public ResponseEntity<Void> assignOrder(@PathVariable Long orderId){
        orderHandler.assignOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{orderId}/ready")
    @Operation(summary = "Mark an Order as READY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Conflicts with mark as ready the order")
    })
    public ResponseEntity<String> markOrderAsReady(@PathVariable Long orderId){
        return ResponseEntity.ok(orderHandler.markOrderAsReady(orderId));
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{orderId}/delivered")
    @Operation(summary = "Mark an Order as DELIVERED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Conflicts with mark as delivered the order")
    })
    public ResponseEntity<String> markOrderAsDelivered(@PathVariable Long orderId,
           @RequestParam() String pin
    ){
        orderHandler.markOrderAsDelivered(orderId, pin);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PatchMapping("/{orderId}/canceled")
    @Operation(summary = "Mark an Order as CANCELED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Conflicts with mark as canceled the order")
    })
    public ResponseEntity<String> markOrderAsCanceled(@PathVariable Long orderId){
        return ResponseEntity.ok(orderHandler.markOrderAsCanceled(orderId));
    }
}
