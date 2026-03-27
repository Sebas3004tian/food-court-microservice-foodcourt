package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;

import java.util.List;
import java.util.Optional;


public interface IOrderPersistencePort {
    Order createOrder(Order order);
    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statusList);

    List<Order> findByRestaurantIdAndStatusPaged(Long restaurantId, OrderStatus orderStatus, int page, int size);

    Optional<Order> findOneById(Long orderId);

    Order updateOrder(Order order);
}
