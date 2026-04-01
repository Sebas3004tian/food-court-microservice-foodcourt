package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.OrderTraceabilityRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderTraceability;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceabilityMapper {

    OrderTraceabilityRequestDto toDto(OrderTraceability orderTraceability);
}
