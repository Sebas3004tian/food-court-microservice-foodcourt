package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderServicePort {

    Order createOrder(Long userId, Order order, List<OrderDish> orderDishList);
    Page<Order> getOrderPagedByStatus(Long userId, Long restaurantId, String status, int page, int size);
    Order assignOrder(Long userId, Order order);
    String markOrderAsReady(Long userId, Order order);
    boolean markOrderAsCanceled(Long userId, Order order);
    void markOrderAsDelivered(Long userId, Order order, String pin);

    List<Long> getOrdersIdsByRestaurantId(Long restaurantId);

    Order getOrderById(Long orderId);
}
