package com.foodcourt.food_court_microservice_foodcourt.domain.validator;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ClientHasActiveOrderException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

public class OrderValidator {

    private OrderValidator() {}

    public static void validateSameRestaurant(Employee employee, Order order) {
        if (!order.getRestaurant().getId().equals(employee.getRestaurant().getId())) {
            throw new UnauthorizedException("You are not an employee of the restaurant order");
        }
    }

    public static void validateOrderStatus(Order order, OrderStatus expectedStatus) {
        if (order.getStatus() != expectedStatus) {
            throw new InvalidOrderStatusException("The order must be "+expectedStatus);
        }
    }

    public static void validateAssignedEmployee(Order order, Employee employee) {
        if (!order.getEmployeeId().equals(employee.getId())) {
            throw new UnauthorizedException("You are not assigned to this order");
        }
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }
    }

    public static void validateClientHasNoActiveOrders(boolean hasActiveOrders) {
        if (hasActiveOrders) {
            throw new ClientHasActiveOrderException("Client cannot create a new order while having an active order");
        }
    }
}