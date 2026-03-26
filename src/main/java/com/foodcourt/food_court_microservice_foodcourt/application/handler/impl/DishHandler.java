package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IDishHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IDishRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;

    private final IDishRequestMapper dishRequestMapper;

    @Override
    public void createDish(CreateDishRequestDto createDishRequestDto) {
        Dish dish = dishRequestMapper.toDish(createDishRequestDto);
        dishServicePort.createDish(dish);
    }

    @Override
    public void updateDish(Long dishId, UpdateDishRequestDto updateDishRequestDto) {
        dishServicePort.updateDish(
                dishId,
                updateDishRequestDto.getPrice(),
                updateDishRequestDto.getDescription()
        );
    }
}
