package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
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
        order.setClientId(clientId);

        Long restaurantId = order.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new NoDataFoundException("Not found the Restaurant with id "+restaurantId));

        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setSecurityPin("Pin ultra secret");

        order = orderPersistencePort.createOrder(order);

        for (OrderDish orderDish : orderDishList) {

            orderDish.setOrder(order);

            Long dishId = orderDish.getDish().getId();
            Dish dish = dishPersistencePort.findOneById(dishId)
                    .orElseThrow(() -> new NoDataFoundException("Not found the Dish with id "+dishId));
            orderDish.setDish(dish);
            orderDish.setPrice(orderDish.calculateTotal());
        }

        orderDishPersistencePort.createOrderDishList(orderDishList);
    }

}
