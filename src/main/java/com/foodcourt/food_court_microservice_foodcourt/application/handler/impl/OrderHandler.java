package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IOrderHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;

    private final IOrderRequestMapper orderRequestMapper;

    @Override
    public void createOrder(CreateOrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        List<OrderDish> orderDishList = orderRequestMapper.toOrderDishList(orderRequestDto.getDishes());
        orderServicePort.createOrder(order, orderDishList);
    }
}
