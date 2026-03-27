package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ClientHasActiveOrderException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;

import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    private final IJwtServicePort jwtServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IJwtServicePort jwtServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.jwtServicePort = jwtServicePort;
    }

    @Override
    public void createOrder(Order order, List<OrderDish> orderDishList) {
        Long clientId = jwtServicePort.getAuthenticatedUserId();

        boolean hasActiveOrders = orderPersistencePort.existsByClientIdAndStatusIn(clientId,
                List.of(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, OrderStatus.LISTO));

        if (hasActiveOrders) {
            throw new ClientHasActiveOrderException("Client cannot create a new order while having an active order");
        }

        Long restaurantId = order.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Restaurant with id "+restaurantId));

        Order orderToSave = Order.createPendingOrder(order,clientId, restaurant, "Pin ultra secret");

        Order persistedOrder = orderPersistencePort.createOrder(orderToSave);

        List<OrderDish> preparedDishes = prepareDishes(restaurantId, orderDishList, persistedOrder);

        orderDishPersistencePort.createOrderDishList(preparedDishes);
    }

    private List<OrderDish> prepareDishes(Long restaurantId, List<OrderDish> orderDishList, Order persistedOrder) {

        for (OrderDish orderDish : orderDishList) {

            orderDish.setOrder(persistedOrder);

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
