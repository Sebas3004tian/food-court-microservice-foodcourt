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

    private final IJwtServicePort jwtServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IEmployeePersistencePort employeePersistencePort, IJwtServicePort jwtServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.employeePersistencePort = employeePersistencePort;
        this.jwtServicePort = jwtServicePort;
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

        Long userId  = jwtServicePort.getAuthenticatedUserId();
        Employee employee = employeePersistencePort.findOneByUserId(userId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Employee with id "+userId));
        Long restaurantId = employee.getRestaurant().getId();

        OrderStatus orderStatus;

        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidOrderStatusException("Invalid order status: " + status);
        }

        return orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurantId,
                orderStatus,
                page,
                size
        );
    }

    @Override
    public void assignOrder(Long orderId) {
        Long userId = jwtServicePort.getAuthenticatedUserId();

        Employee employee = employeePersistencePort.findOneByUserId(userId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Employee with id "+userId));

        Order order = orderPersistencePort.findOneById(orderId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Order with id "+orderId));

        if (!order.getRestaurant().getId().equals(employee.getRestaurant().getId())) {
            throw new UnauthorizedException("You are not a employee of the restaurant order");
        }

        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new InvalidOrderStatusException("The order have to has PENDING status");
        }

        order.setEmployeeId(employee.getId());

        order.setStatus(OrderStatus.EN_PREPARACION);

        orderPersistencePort.updateOrder(order);
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
