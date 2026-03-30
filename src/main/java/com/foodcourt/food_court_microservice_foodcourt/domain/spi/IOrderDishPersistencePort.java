package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;

import java.util.List;

public interface IOrderDishPersistencePort {
    List<OrderDish> createOrderDishList(List<OrderDish> orderDishList, Order order);
}
