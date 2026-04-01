package com.foodcourt.food_court_microservice_foodcourt.domain.api;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderTraceability;

public interface ITraceabilityServicePort {
    void saveOrderTraceability(OrderTraceability orderTraceability);
}
