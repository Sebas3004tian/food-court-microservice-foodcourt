package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private static final SecureRandom RANDOM = new SecureRandom();

    private Long id;
    private Long clientId;
    private Restaurant restaurant;
    private OrderStatus status;
    private Long employeeId;
    private String securityPin;

    private List<OrderDish> orderDishes;

    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;

    public static Order createPendingOrder(Order order, Long clientId, Restaurant restaurant) {
        order.setClientId(clientId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setSecurityPin("------");
        return order;
    }

    public String markAsReady() {
        String pin =generatePin();
        this.status = OrderStatus.LISTO;
        this.securityPin = generatePin();
        return pin;
    }

    private static String generatePin() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }
}