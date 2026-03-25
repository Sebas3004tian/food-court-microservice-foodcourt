package com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.RestaurantUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter.RestaurantJpaAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantJpaAdapter(restaurantRepository,restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase(
                restaurantPersistencePort()
        );
    }
}
