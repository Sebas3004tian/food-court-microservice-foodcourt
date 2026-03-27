package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IOrderDishEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderDishJpaAdapter implements IOrderDishPersistencePort {

    private final IOrderDishRepository orderDishRepository;

    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Override
    public List<OrderDish> createOrderDishList(List<OrderDish> orderDishList) {
        List<OrderDishEntity> mapeo2=orderDishEntityMapper.toEntityList(orderDishList);

        List<OrderDishEntity> orderDishEntityList = orderDishRepository.saveAll(
                mapeo2
        );
        return orderDishEntityMapper.toOrderDishList(orderDishEntityList);
    }
}
