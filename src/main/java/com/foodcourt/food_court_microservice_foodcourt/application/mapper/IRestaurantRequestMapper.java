package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateRestaurantRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {
    Restaurant toRestaurant(CreateRestaurantRequestDto restaurantRequestDto);
}
