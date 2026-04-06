package com.foodcourt.food_court_microservice_foodcourt.infraestructure.input.rest;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.EmployeeEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderEfficiencyResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.ITraceabilityHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traceability")
@RequiredArgsConstructor
public class TraceabilityRestController {

    private final ITraceabilityHandler traceabilityHandler;

    @GetMapping("/efficiency/orders")
    @Operation(summary = "Get efficiency of the orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20a", description = "Receive efficiency of orders of the restaurant"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public ResponseEntity<List<OrderEfficiencyResponseDto>> getOrderEfficiency(){
        return ResponseEntity.ok(traceabilityHandler.getOrderEfficiency());
    }

    @GetMapping("/efficiency/employees")
    @Operation(summary = "Get efficiency of the employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20a", description = "Receive efficiency of employees of the restaurant"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public ResponseEntity<List<EmployeeEfficiencyResponseDto>> getEmployeesRanking() {
        return ResponseEntity.ok(traceabilityHandler.getEmployeeRanking());
    }


}