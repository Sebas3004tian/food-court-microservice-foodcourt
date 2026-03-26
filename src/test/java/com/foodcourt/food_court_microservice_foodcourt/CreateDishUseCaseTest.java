package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateDishUseCaseTest {


    @Test
    void shouldCreateDishWithAllArgsConstructor() {
        Category category = new Category(1L, "POSTRES");
        Restaurant restaurant = new Restaurant(
                1L,
                "Restaurante Test",
                123456L,
                "Calle 123",
                "123456789",
                "logo.png",
                10L
        );

        Dish dish = new Dish(
                1L,
                "Helado",
                "Helado de vainilla",
                "url.com/img",
                true,
                restaurant,
                category
        );

        assertEquals(1L, dish.getId());
        assertEquals("Helado", dish.getName());
        assertEquals("Helado de vainilla", dish.getDescription());
        assertEquals("url.com/img", dish.getUrlImage());
        assertTrue(dish.isActive());
        assertEquals(restaurant, dish.getRestaurant());
        assertEquals(category, dish.getCategory());
    }

    @Test
    void shouldSetAndGetValuesCorrectly() {
        Dish dish = new Dish();

        dish.setId(2L);
        dish.setName("Pizza");
        dish.setDescription("Pizza grande");
        dish.setUrlImage("img.png");
        dish.setActive(false);

        assertEquals(2L, dish.getId());
        assertEquals("Pizza", dish.getName());
        assertEquals("Pizza grande", dish.getDescription());
        assertEquals("img.png", dish.getUrlImage());
        assertFalse(dish.isActive());
    }

    @Test
    void shouldActivateDish() {
        Dish dish = new Dish();
        dish.setActive(false);

        dish.activate();

        assertTrue(dish.isActive());
    }
}
