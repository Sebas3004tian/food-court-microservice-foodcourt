package com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.RestaurantUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter.UserFeignAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.IUserFeignClient;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter.CategoryJpaAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter.DishJpaAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter.RestaurantJpaAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.ICategoryEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IDishEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.ICategoryRepository;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IDishRepository;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IRestaurantRepository;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.security.adapter.JwtServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IDishRepository dishRepository;
    private final ICategoryRepository categoryRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryEntityMapper categoryEntityMapper;

    private final IUserFeignClient userFeignClient;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantJpaAdapter(restaurantRepository,restaurantEntityMapper);
    }

    @Bean
    public IDishPersistencePort dishPersistencePort(){
        return new DishJpaAdapter(dishRepository,dishEntityMapper);
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort(){
        return new CategoryJpaAdapter(categoryRepository,categoryEntityMapper);
    }

    @Bean
    public IJwtServicePort jwtServicePort(){
        return new JwtServiceAdapter();
    }

    @Bean
    public IUserExternalPort userExternalPort(){
        return new UserFeignAdapter(userFeignClient);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase(
                restaurantPersistencePort(),
                userExternalPort()
        );
    }

    @Bean
    public IDishServicePort dishServicePort(){
        return new DishUseCase(
                dishPersistencePort(),
                restaurantPersistencePort(),
                categoryPersistencePort(),
                jwtServicePort()
        );
    }
}