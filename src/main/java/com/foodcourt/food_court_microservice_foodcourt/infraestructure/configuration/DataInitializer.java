package com.foodcourt.food_court_microservice_foodcourt.infraestructure.configuration;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.CategoryEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.ICategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class DataInitializer {


    private static final String APPETIZER_CATEGORY = "ENTRADAS";
    private static final String MAIN_COURSE_CATEGORY = "PLATOS_FUERTES";
    private static final String DESSERT_CATEGORY = "POSTRES";
    private static final String BEVERAGE_CATEGORY = "BEBIDAS";
    private static final String FAST_FOOD_CATEGORY = "COMIDAS_RAPIDAS";
    private static final String SOUP_CATEGORY = "SOPAS";
    private static final String SALAD_CATEGORY = "ENSALADAS";
    private static final String VEGAN_CATEGORY = "VEGANO_VEGETARIANO";


    @Bean
    @Order(1)
    CommandLineRunner initCategories(ICategoryRepository categoryRepository) {
        return args -> {

            List<String> categories = List.of(
                    APPETIZER_CATEGORY,
                    MAIN_COURSE_CATEGORY,
                    DESSERT_CATEGORY,
                    BEVERAGE_CATEGORY,
                    FAST_FOOD_CATEGORY,
                    SOUP_CATEGORY,
                    SALAD_CATEGORY,
                    VEGAN_CATEGORY
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
