package com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IEmployeeServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IOrderServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.EmployeeUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.RestaurantUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter.SmsFeignAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter.UserFeignAdapter;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ISmsFeignClient;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.IUserFeignClient;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter.*;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.*;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.*;
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
    private final IOrderRepository orderRepository;
    private final IOrderDishRepository orderDishRepository;
    private final IEmployeeRepository employeeRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final IEmployeeEntityMapper employeeEntityMapper;

    private final IUserFeignClient userFeignClient;
    private final ISmsFeignClient smsFeignClient;

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
    public IOrderPersistencePort orderPersistencePort(){
        return new OrderJpaAdapter(orderRepository,orderDishRepository,orderEntityMapper);
    }

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort(){
        return new OrderDishJpaAdapter(orderDishRepository,orderDishEntityMapper,orderEntityMapper);
    }
    @Bean
    public IEmployeePersistencePort employeePersistencePort(){
        return new EmployeeJpaAdapter(employeeRepository,employeeEntityMapper);
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
    public ISmsClientPort smsClientPort(){
        return new SmsFeignAdapter(smsFeignClient);
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
                categoryPersistencePort()
        );
    }

    @Bean
    public IOrderServicePort orderServicePort(){
        return new OrderUseCase(
                orderPersistencePort(),
                orderDishPersistencePort(),
                dishPersistencePort(),
                restaurantPersistencePort(),
                employeePersistencePort(),
                smsClientPort(),
                userExternalPort()
        );
    }

    @Bean
    public IEmployeeServicePort employeeServicePort(){
        return new EmployeeUseCase(
                employeePersistencePort(),
                restaurantPersistencePort(),
                userExternalPort()

        );
    }
}