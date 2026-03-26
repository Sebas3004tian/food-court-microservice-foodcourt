package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IOrderEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;

    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = orderRepository.save(orderEntityMapper.toEntity(order));
        return orderEntityMapper.toOrder(orderEntity);
    }
}
