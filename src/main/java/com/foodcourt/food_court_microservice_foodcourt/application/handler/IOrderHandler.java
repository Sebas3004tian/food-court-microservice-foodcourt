package com.foodcourt.food_court_microservice_foodcourt.application.handler;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;

public interface IOrderHandler {
    void createOrder(CreateOrderRequestDto createOrderRequestDto);
    PageResponseDto<OrderResponseDto> getOrderPagedByStatus(String status, int page, int size);
    void assignOrder(Long orderId);
    String markOrderAsReady(Long orderId);
    String markOrderAsCanceled(Long orderId);
    void markOrderAsDelivered(Long orderId, String pin);
}
