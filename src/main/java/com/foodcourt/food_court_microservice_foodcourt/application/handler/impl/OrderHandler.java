package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IOrderHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderResponseMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
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
    private final IOrderResponseMapper orderResponseMapper;

    private final IJwtServicePort jwtServicePort;

    @Override
    public void createOrder(CreateOrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        List<OrderDish> orderDishList = orderRequestMapper.toOrderDishList(orderRequestDto.getDishes());
        orderServicePort.createOrder(getAuthUserId(),order, orderDishList);
    }

    @Override
    public List<OrderResponseDto> getOrderPagedByStatus(String status, int page, int size) {
        return orderResponseMapper.toResponseList(orderServicePort.getOrderPagedByStatus(getAuthUserId(),status,page,size));
    }

    @Override
    public void assignOrder(Long orderId) {
        orderServicePort.assignOrder(getAuthUserId(),orderId);
    }

    @Override
    public String markOrderAsReady(Long orderId) {
        return orderServicePort.markOrderAsReady(getAuthUserId(),orderId);
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String pin) {
        orderServicePort.markOrderAsDelivered(getAuthUserId(), orderId, pin);
    }

    public Long getAuthUserId(){
        return jwtServicePort.getAuthenticatedUserId();
    }
}
