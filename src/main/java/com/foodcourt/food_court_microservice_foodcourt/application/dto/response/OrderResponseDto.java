package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private Long id;
    private Long clientId;
    private RestaurantResponseDto restaurant;
    private OrderStatus status;
    private Long employeeId;

    private List<OrderDishResponseDto> orderDishes;

    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;
}
