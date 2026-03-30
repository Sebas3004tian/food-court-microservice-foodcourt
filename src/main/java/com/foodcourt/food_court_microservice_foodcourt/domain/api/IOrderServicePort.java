package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;

import java.util.List;

public interface IOrderServicePort {

    void createOrder(Order order, List<OrderDish> orderDishList);
    List<Order> getOrderPagedByStatus(String status, int page, int size);
    void assignOrder(Long orderId);
}
