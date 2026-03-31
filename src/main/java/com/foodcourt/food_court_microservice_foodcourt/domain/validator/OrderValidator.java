package com.foodcourt.food_court_microservice_foodcourt.domain.validator;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ClientHasActiveOrderException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidPinException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.NotAssignedException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

public class OrderValidator {

    private OrderValidator() {}

    public static void validateSameRestaurant(Employee employee, Order order) {
        if (!order.getRestaurant().getId().equals(employee.getRestaurant().getId())) {
            throw new UnauthorizedException();
        }
    }

    public static void validateOrderStatus(Order order, OrderStatus expectedStatus) {
        if (order.getStatus() != expectedStatus) {
            throw new InvalidOrderStatusException(expectedStatus.name());
        }
    }

    public static void validateAssignedEmployee(Order order, Employee employee) {
        if (!order.getEmployeeId().equals(employee.getId())) {
            throw new NotAssignedException();
        }
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }
    }

    public static void validateClientHasNoActiveOrders(boolean hasActiveOrders) {
        if (hasActiveOrders) {
            throw new ClientHasActiveOrderException();
        }
    }

    public static void validateSecurityPin(Order order, String pin) {
        if (!order.getSecurityPin().equals(pin)) {
            throw new InvalidPinException(pin);
        }
    }
}