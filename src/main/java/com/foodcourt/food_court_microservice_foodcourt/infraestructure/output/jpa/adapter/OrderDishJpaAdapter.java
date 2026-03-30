package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IOrderDishEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IOrderEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderDishJpaAdapter implements IOrderDishPersistencePort {

    private final IOrderDishRepository orderDishRepository;

    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public List<OrderDish> createOrderDishList(List<OrderDish> orderDishList, Order order) {

        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        List<OrderDishEntity> orderDishEntityList =
                orderDishEntityMapper.toEntityList(orderDishList);

        for (OrderDishEntity orderDishEntity : orderDishEntityList) {
            orderDishEntity.setOrder(orderEntity);
        }

        return orderDishEntityMapper.toOrderDishList(orderDishRepository.saveAll(orderDishEntityList));
    }
}
