package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.UpdateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.DishResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IDishHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IDishRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IDishResponseMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;

    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    private final IJwtServicePort jwtServicePort;

    @Override
    public void createDish(CreateDishRequestDto createDishRequestDto) {
        Dish dish = dishRequestMapper.toDish(createDishRequestDto);
        Long userId = jwtServicePort.getAuthenticatedUserId();
        dishServicePort.createDish(userId,dish);
    }

    @Override
    public void updateDish(Long dishId, UpdateDishRequestDto updateDishRequestDto) {
        Long userId = jwtServicePort.getAuthenticatedUserId();
        dishServicePort.updateDish(
                userId,
                dishId,
                updateDishRequestDto.getPrice(),
                updateDishRequestDto.getDescription()
        );
    }

    @Override
    public void enableOrDisableDish(Long dishId, boolean active) {
        Long userId = jwtServicePort.getAuthenticatedUserId();
        dishServicePort.enableOrDisableDish(userId,dishId,active);
    }


    @Override
    public PageResponseDto<DishResponseDto> getDishesPagedByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        Page<Dish> dishPage = dishServicePort.getDishesPagedByRestaurant(restaurantId,categoryId,page,size);

        PageResponseDto<DishResponseDto> pageResponseDto = new PageResponseDto<>();

        pageResponseDto.setContent(
                dishResponseMapper.toResponseList(dishPage.getContent())
        );
        pageResponseDto.setPage(dishPage.getNumber());
        pageResponseDto.setSize(dishPage.getSize());
        pageResponseDto.setTotalElements(dishPage.getTotalElements());
        pageResponseDto.setTotalPages(dishPage.getTotalPages());
        pageResponseDto.setFirst(dishPage.isFirst());
        pageResponseDto.setLast(dishPage.isLast());

        return pageResponseDto;
    }
}
