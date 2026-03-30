package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private Long clientId;
    private Restaurant restaurant;
    private OrderStatus status;
    private Long employeeId;
    private String securityPin;

    private List<OrderDish> orderDishes;

    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;

    public static Order createPendingOrder(Order order, Long clientId, Restaurant restaurant, String securityPin) {
        order.setClientId(clientId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setSecurityPin(securityPin);
        return order;
    }
}