package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ISmsServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.validator.OrderValidator;
import org.springframework.data.domain.Page;

import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IEmployeePersistencePort employeePersistencePort;

    private final ISmsServicePort smsServicePort;
    private final IUserServicePort userServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IEmployeePersistencePort employeePersistencePort, ISmsServicePort smsServicePort, IUserServicePort userServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.employeePersistencePort = employeePersistencePort;
        this.smsServicePort = smsServicePort;
        this.userServicePort = userServicePort;
    }

    private Employee getAuthenticatedEmployee(Long userId) {
        return employeePersistencePort.findOneByUserId(userId)
                .orElseThrow(() -> new EmployeeNotFoundException(userId.toString()));
    }

    private Order getOrder(Long orderId) {
        return orderPersistencePort.findOneById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
    }

    private String sendReadyOrderSms(Long userId, String pin) {
        String phoneNumber = userServicePort.getPhone(userId);

        if (phoneNumber == null) {
            return "Order marked as ready but SMS failed  (user service error)";
        }

        String smsResponse = smsServicePort.sendSms(
                phoneNumber,
                "Tu pedido está listo. PIN: " + pin
        );

        if (smsResponse == null) {
            return "Order marked as ready but SMS failed (sms service error)";
        }

        return "Order marked as ready and SMS sent successfully: " + smsResponse;
    }


    @Override
    public void createOrder(Long clientId, Order order, List<OrderDish> orderDishList) {

        boolean hasActiveOrders = orderPersistencePort.existsByClientIdAndStatusIn(clientId,
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, OrderStatus.LISTO));
        OrderValidator.validateClientHasNoActiveOrders(hasActiveOrders);

        Long restaurantId = order.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId.toString()));

        Order orderToSave = Order.createPendingOrder(order, clientId, restaurant);
        Order persistedOrder = orderPersistencePort.createOrder(orderToSave);

        List<OrderDish> preparedDishes = prepareDishes(restaurantId, orderDishList);
        orderDishPersistencePort.createOrderDishList(preparedDishes, persistedOrder);
    }

    @Override
    public Page<Order> getOrderPagedByStatus(Long userId, String status, int page, int size) {
        OrderValidator.validatePaginationParams(page, size);

        Employee employee = getAuthenticatedEmployee(userId);

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidOrderStatusException(status);
        }

        return orderPersistencePort.findByRestaurantIdAndStatusPaged(
                employee.getRestaurant().getId(),
                orderStatus,
                page,
                size
        );
    }

    @Override
    public void assignOrder(Long userId,Long orderId) {
        Employee employee = getAuthenticatedEmployee(userId);
        Order order = getOrder(orderId);

        OrderValidator.validateSameRestaurant(employee, order);
        OrderValidator.validateOrderStatus(order, OrderStatus.PENDIENTE);

        order.setEmployeeId(employee.getId());
        order.setStatus(OrderStatus.EN_PREPARACION);

        orderPersistencePort.updateOrder(order);
    }

    @Override
    public String markOrderAsReady(Long userId,Long orderId) {
        Employee employee = getAuthenticatedEmployee(userId);
        Order order = getOrder(orderId);

        OrderValidator.validateSameRestaurant(employee, order);
        OrderValidator.validateOrderStatus(order, OrderStatus.EN_PREPARACION);
        OrderValidator.validateAssignedEmployee(order, employee);

        order.markAsReady();
        orderPersistencePort.updateOrder(order);
        String pin = order.getSecurityPin();

        return sendReadyOrderSms(order.getClientId(), pin);
    }

    @Override
    public void markOrderAsDelivered(Long userId, Long orderId, String pin) {
        Employee employee = getAuthenticatedEmployee(userId);
        Order order = getOrder(orderId);

        OrderValidator.validateSameRestaurant(employee, order);
        OrderValidator.validateOrderStatus(order, OrderStatus.LISTO);
        OrderValidator.validateAssignedEmployee(order, employee);
        OrderValidator.validateSecurityPin(order,pin);

        order.markAsDelivered();
        orderPersistencePort.updateOrder(order);
    }

    private List<OrderDish> prepareDishes(Long restaurantId, List<OrderDish> orderDishList) {

        for (OrderDish orderDish : orderDishList) {

            if (orderDish.getAmount() == null || orderDish.getAmount() <= 0) {
                throw new IllegalArgumentException("Invalid amount");
            }

            Long dishId = orderDish.getDish().getId();
            Dish dish = dishPersistencePort.findOneById(dishId)
                    .orElseThrow(() -> new DishNotFoundException(dishId.toString()));

            if (!dish.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("All dishes must belong to the same restaurant");
            }

            orderDish.setDish(dish);
            orderDish.setPrice(orderDish.calculateTotal());
        }
        return orderDishList;
    }

}
