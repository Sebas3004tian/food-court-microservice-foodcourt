package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IOrderHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderResponseMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public PageResponseDto<OrderResponseDto> getOrderPagedByStatus(String status, int page, int size) {
        Page<Order> dishPage = orderServicePort.getOrderPagedByStatus(getAuthUserId(),status,page,size);

        PageResponseDto<OrderResponseDto> pageResponseDto = new PageResponseDto<>();

        pageResponseDto.setContent(
                orderResponseMapper.toResponseList(dishPage.getContent())
        );
        pageResponseDto.setPage(dishPage.getNumber());
        pageResponseDto.setSize(dishPage.getSize());
        pageResponseDto.setTotalElements(dishPage.getTotalElements());
        pageResponseDto.setTotalPages(dishPage.getTotalPages());
        pageResponseDto.setFirst(dishPage.isFirst());
        pageResponseDto.setLast(dishPage.isLast());

        return pageResponseDto;
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
    public String markOrderAsCanceled(Long orderId) {
        return orderServicePort.markOrderAsCanceled(getAuthUserId(),orderId);
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String pin) {
        orderServicePort.markOrderAsDelivered(getAuthUserId(), orderId, pin);
    }

    public Long getAuthUserId(){
        return jwtServicePort.getAuthenticatedUserId();
    }
}