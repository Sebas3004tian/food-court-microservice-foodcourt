package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.validator.OrderValidator;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;

import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IEmployeePersistencePort employeePersistencePort;

    private final ISmsClientPort smsClientPort;
    private final IUserExternalPort userExternalPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IEmployeePersistencePort employeePersistencePort, ISmsClientPort smsClientPort, IUserExternalPort userExternalPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.employeePersistencePort = employeePersistencePort;
        this.smsClientPort = smsClientPort;
        this.userExternalPort = userExternalPort;
    }

    private Employee getAuthenticatedEmployee(Long userId) {
        return employeePersistencePort.findOneByUserId(userId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Employee with id " + userId));
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderPersistencePort.findOneById(orderId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Order with id " + orderId));
    }

    private String sendReadyOrderSms(Long userId, String pin) {
        String phoneNumber = userExternalPort.getPhone(userId);

        if (phoneNumber == null) {
            return "Order marked as ready but SMS failed  (user service error)";
        }

        String smsResponse = smsClientPort.sendSms(
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
                .orElseThrow(() -> new NoDataFoundException("Not found the Restaurant with id "+restaurantId));

        Order orderToSave = Order.createPendingOrder(order, clientId, restaurant);
        Order persistedOrder = orderPersistencePort.createOrder(orderToSave);

        List<OrderDish> preparedDishes = prepareDishes(restaurantId, orderDishList);
        orderDishPersistencePort.createOrderDishList(preparedDishes, persistedOrder);
    }

    @Override
    public List<Order> getOrderPagedByStatus(Long userId,String status, int page, int size) {
        OrderValidator.validatePaginationParams(page, size);

        Employee employee = getAuthenticatedEmployee(userId);

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidOrderStatusException("Invalid order status: " + status);
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
        Order order = getOrderOrThrow(orderId);

        OrderValidator.validateSameRestaurant(employee, order);
        OrderValidator.validateOrderStatus(order, OrderStatus.PENDIENTE);

        order.setEmployeeId(employee.getId());
        order.setStatus(OrderStatus.EN_PREPARACION);

        orderPersistencePort.updateOrder(order);
    }

    @Override
    public String markOrderAsReady(Long userId,Long orderId) {
        Employee employee = getAuthenticatedEmployee(userId);
        Order order = getOrderOrThrow(orderId);

        OrderValidator.validateSameRestaurant(employee, order);
        OrderValidator.validateOrderStatus(order, OrderStatus.EN_PREPARACION);
        OrderValidator.validateAssignedEmployee(order, employee);

        String pin = order.markAsReady();
        orderPersistencePort.updateOrder(order);

        return sendReadyOrderSms(order.getClientId(), pin);
    }

    private List<OrderDish> prepareDishes(Long restaurantId, List<OrderDish> orderDishList) {

        for (OrderDish orderDish : orderDishList) {

            if (orderDish.getAmount() == null || orderDish.getAmount() <= 0) {
                throw new IllegalArgumentException("Invalid amount");
            }

            Long dishId = orderDish.getDish().getId();
            Dish dish = dishPersistencePort.findOneById(dishId)
                    .orElseThrow(() -> new NoDataFoundException("Not found the Dish with id "+dishId));

            if (!dish.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("All dishes must belong to the same restaurant");
            }

            orderDish.setDish(dish);
            orderDish.setPrice(orderDish.calculateTotal());
        }
        return orderDishList;
    }

}
