package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.RestaurantResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {

    RestaurantResponseDto toResponse(Restaurant restaurant);

    List<RestaurantResponseDto> toResponseList(List<Restaurant> restaurantList);

}
