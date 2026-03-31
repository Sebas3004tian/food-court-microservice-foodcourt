package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;

import java.util.List;

public interface IOrderServicePort {

    void createOrder(Long userId, Order order, List<OrderDish> orderDishList);
    List<Order> getOrderPagedByStatus(Long userId, String status, int page, int size);
    void assignOrder(Long userId, Long orderId);
    String markOrderAsReady(Long userId, Long orderId);
}
