package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {

    @Mapping(source = "restaurantId", target = "restaurant.id")
    @Mapping(source = "categoryId", target = "category.id")
    Dish toDish(CreateDishRequestDto createDishRequestDto);
}
