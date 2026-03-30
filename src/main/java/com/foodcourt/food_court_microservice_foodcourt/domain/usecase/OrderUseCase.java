package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ClientHasActiveOrderException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IEmployeePersistencePort employeePersistencePort;

    private final ISmsClientPort smsClientPort;
    private final IUserExternalPort userExternalPort;

    private final IJwtServicePort jwtServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IEmployeePersistencePort employeePersistencePort, ISmsClientPort smsClientPort, IUserExternalPort userExternalPort, IJwtServicePort jwtServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.employeePersistencePort = employeePersistencePort;
        this.smsClientPort = smsClientPort;
        this.userExternalPort = userExternalPort;
        this.jwtServicePort = jwtServicePort;
    }

    private Employee getAuthenticatedEmployee() {
        Long userId = jwtServicePort.getAuthenticatedUserId();
        return employeePersistencePort.findOneByUserId(userId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Employee with id " + userId));
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderPersistencePort.findOneById(orderId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Order with id " + orderId));
    }

    private void validateSameRestaurant(Employee employee, Order order) {
        if (!order.getRestaurant().getId().equals(employee.getRestaurant().getId())) {
            throw new UnauthorizedException("You are not an employee of the restaurant order");
        }
    }

    private void validateOrderStatus(Order order, OrderStatus expectedStatus, String message) {
        if (order.getStatus() != expectedStatus) {
            throw new InvalidOrderStatusException(message);
        }
    }

    private void validateAssignedEmployee(Order order, Employee employee) {
        if (!order.getEmployeeId().equals(employee.getId())) {
            throw new UnauthorizedException("You are not assigned to this order");
        }
    }

    private String sendReadyOrderSms(Long userId, String pin) {
        String phoneNumber = userExternalPort.getPhone(userId);

        if (phoneNumber == null) {
            return "Order marked as ready but SMS failed  (user service error)";
        }

        String response = smsClientPort.sendSms(
                phoneNumber,
                "Tu pedido está listo. PIN: " + pin
        );

        if (response == null) {
            return "Order marked as ready but SMS failed (sms service error)";
        }

        return "Order marked as ready and SMS sent successfully: " + response;
    }


    @Override
    public void createOrder(Order order, List<OrderDish> orderDishList) {
        Long clientId = jwtServicePort.getAuthenticatedUserId();

        validateClientHasNoActiveOrders(clientId);

        Long restaurantId = order.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Restaurant with id "+restaurantId));

        Order orderToSave = Order.createPendingOrder(order,clientId, restaurant, "Pin ultra secret");

        Order persistedOrder = orderPersistencePort.createOrder(orderToSave);

        List<OrderDish> preparedDishes = prepareDishes(restaurantId, orderDishList);

        orderDishPersistencePort.createOrderDishList(preparedDishes,persistedOrder);
    }

    @Override
    public List<Order> getOrderPagedByStatus(String status, int page, int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination params");
        }

        Employee employee = getAuthenticatedEmployee();

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
    public void assignOrder(Long orderId) {
        Employee employee = getAuthenticatedEmployee();
        Order order = getOrderOrThrow(orderId);

        validateSameRestaurant(employee, order);
        validateOrderStatus(order, OrderStatus.PENDIENTE, "The order must be PENDING");

        order.setEmployeeId(employee.getId());
        order.setStatus(OrderStatus.EN_PREPARACION);

        orderPersistencePort.updateOrder(order);
    }

    @Override
    public String markOrderAsReady(Long orderId) {
        Employee employee = getAuthenticatedEmployee();
        Order order = getOrderOrThrow(orderId);

        validateSameRestaurant(employee, order);
        validateOrderStatus(order, OrderStatus.EN_PREPARACION, "The order must be IN_PREPARATION");
        validateAssignedEmployee(order, employee);

        String pin = generatePin();

        order.setStatus(OrderStatus.LISTO);
        order.setSecurityPin(pin);
        orderPersistencePort.updateOrder(order);

        return sendReadyOrderSms(order.getClientId(), pin);
    }

    private String generatePin() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }

    private void validateClientHasNoActiveOrders(Long clientId){
        boolean hasActiveOrders = orderPersistencePort.existsByClientIdAndStatusIn(clientId,
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, OrderStatus.LISTO));

        if (hasActiveOrders) {
            throw new ClientHasActiveOrderException("Client cannot create a new order while having an active order");
        }
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
