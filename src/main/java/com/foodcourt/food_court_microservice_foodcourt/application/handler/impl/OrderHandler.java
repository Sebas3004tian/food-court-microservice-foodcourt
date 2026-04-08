package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IOrderHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IOrderResponseMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.*;
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
    private final IUserServicePort userServicePort;
    private final ISmsServicePort smsServicePort;
    private final ITraceabilityServicePort traceabilityServicePort;

    @Override
    public void createOrder(CreateOrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        List<OrderDish> orderDishList = orderRequestMapper.toOrderDishList(orderRequestDto.getDishes());
        order = orderServicePort.createOrder(getAuthUserId(),order, orderDishList);
        saveTraceability(order,null,OrderStatus.PENDIENTE);
    }

    @Override
    public PageResponseDto<OrderResponseDto> getOrderPagedByStatus(Long restaurantId, String status, int page, int size) {
        Page<Order> dishPage = orderServicePort.getOrderPagedByStatus(getAuthUserId(),restaurantId,status,page,size);

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
        Long userId = getAuthUserId();
        if (!userServicePort.isUserEmployee(userId)) {
            throw new InvalidUserRoleException(UserRole.EMPLEADO.name());
        }
        Order order = orderServicePort.getOrderById(orderId);
        order = orderServicePort.assignOrder(userId,order);
        saveTraceability(order, OrderStatus.PENDIENTE,OrderStatus.EN_PREPARACION);
    }

    @Override
    public String markOrderAsReady(Long orderId) {
        Order order = orderServicePort.getOrderById(orderId);
        String pin = orderServicePort.markOrderAsReady(getAuthUserId(),order);
        String phoneNumber = userServicePort.getPhone(order.getClientId());
        String smsResponse = smsServicePort.sendSms(
                phoneNumber,
                "Tu pedido está listo. PIN: " + pin
        );

        saveTraceability(order, OrderStatus.EN_PREPARACION,OrderStatus.LISTO);
        return SmsResultMessage.ORDER_READY_SUCCESS.getMessage() + ": " + smsResponse;
    }

    @Override
    public String markOrderAsCanceled(Long orderId) {
        Order order = orderServicePort.getOrderById(orderId);
        boolean successfully = orderServicePort.markOrderAsCanceled(getAuthUserId(),order);

        if(!successfully) {
            String phoneNumber = userServicePort.getPhone(order.getClientId());
            return smsServicePort.sendSms(
                    phoneNumber,
                    "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse"
            );
        } else {
            saveTraceability(order, OrderStatus.PENDIENTE,OrderStatus.CANCELADO);
            return  "Orden cancelada";
        }

    }

    @Override
    public void markOrderAsDelivered(Long orderId, String pin) {
        Order order = orderServicePort.getOrderById(orderId);
        orderServicePort.markOrderAsDelivered(getAuthUserId(), order, pin);
        saveTraceability(order, OrderStatus.LISTO,OrderStatus.ENTREGADO);
    }

    public Long getAuthUserId(){
        return jwtServicePort.getAuthenticatedUserId();
    }

    private void saveTraceability(Order order, OrderStatus previousStatus, OrderStatus newStatus){

        Long clientId= order.getClientId();
        String clientEmail=userServicePort.getEmail(clientId);

        Long employeeId= order.getEmployeeId();
        String employeeEmail=null;
        if(employeeId!=null) {
            employeeEmail=userServicePort.getEmail(employeeId);
        }

        String prevStatus = null;
        if(previousStatus!=null){
            prevStatus=previousStatus.name();
        }

        OrderTraceability orderTraceability = new OrderTraceability(
                order.getId(),
                clientId,
                clientEmail,
                prevStatus,
                newStatus.name(),
                employeeId,
                employeeEmail
        );
        traceabilityServicePort.saveOrderTraceability(orderTraceability);

    }
}