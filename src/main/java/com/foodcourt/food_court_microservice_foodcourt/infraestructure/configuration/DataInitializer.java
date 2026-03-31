package com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.DishCategory;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.CategoryEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.ICategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    CommandLineRunner initCategories(ICategoryRepository categoryRepository) {
        return args -> {

            List<String> categories = List.of(
                    DishCategory.ENTRADAS.name(),
                    DishCategory.PLATOS_FUERTES.name(),
                    DishCategory.POSTRES.name(),
                    DishCategory.BEBIDAS.name(),
                    DishCategory.COMIDAS_RAPIDAS.name(),
                    DishCategory.SOPAS.name(),
                    DishCategory.ENSALADAS.name(),
                    DishCategory.VEGANO_VEGETARIANO.name()
            );

            for (String categoryName : categories) {
                boolean exists = categoryRepository.findOneByName(categoryName).isPresent();
                if (!exists) {
                    categoryRepository.save(new CategoryEntity(categoryName));
                }
            }
        };
    }
}
