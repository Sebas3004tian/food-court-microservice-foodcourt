package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
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

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderPersistencePort.findOneById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
    }

    @Override
    public Order createOrder(Long clientId, Order order, List<OrderDish> orderDishList) {

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

        return persistedOrder;
    }

    @Override
    public Page<Order> getOrderPagedByStatus(Long userId, Long restaurantId, String status, int page, int size) {
        OrderValidator.validatePaginationParams(page, size);
        if (restaurantPersistencePort.findOneById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("");
        }

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidOrderStatusException(status);
        }

        return orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurantId,
                orderStatus,
                page,
                size
        );
    }

    @Override
    public Order assignOrder(Long userId,Order order) {

        OrderValidator.validateOrderStatus(order, OrderStatus.PENDIENTE);

        order.setEmployeeId(userId);
        order.setStatus(OrderStatus.EN_PREPARACION);

        return orderPersistencePort.updateOrder(order);
    }

    @Override
    public String markOrderAsReady(Long userId,Order order) {
        OrderValidator.validateOrderStatus(order, OrderStatus.EN_PREPARACION);
        OrderValidator.validateAssignedEmployee(order, userId);

        order.markAsReady();
        String pin = order.getSecurityPin();
        orderPersistencePort.updateOrder(order);
        return pin;
    }

    @Override
    public boolean markOrderAsCanceled(Long clientId, Order order) {
        OrderValidator.validateSameClient(clientId, order);
        if(order.getStatus() != OrderStatus.PENDIENTE) {
            return false;
        } else {
            order.markAsCanceled();
            orderPersistencePort.updateOrder(order);
            return true;
        }
    }

    @Override
    public void markOrderAsDelivered(Long userId,Order order, String pin) {

        OrderValidator.validateOrderStatus(order, OrderStatus.LISTO);
        OrderValidator.validateAssignedEmployee(order, userId);
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

    @Override
    public List<Long> getOrdersIdsByRestaurantId(Long restaurantId) {
        List<Long> orderIds = orderPersistencePort.findOrdersIdsByRestaurantId(restaurantId);
        if ( orderIds.isEmpty()){
            throw new RestaurantNotFoundException("");
        }
        return orderIds;
    }


}
