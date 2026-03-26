package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;


public interface IOrderPersistencePort {
    Order createOrder(Order order);
}
