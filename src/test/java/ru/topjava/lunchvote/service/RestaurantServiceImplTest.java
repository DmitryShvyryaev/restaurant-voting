package ru.topjava.lunchvote.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;

import static org.junit.Assert.assertThrows;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;

public class RestaurantServiceImplTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantService.getAll(), restaurants);
    }

    @Test
    public void get() {
        Restaurant restaurant = restaurantService.get(START_SEQ_REST);
        RESTAURANT_MATCHER.assertMatch(restaurant, rest1);
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(1));
    }

    @Test
    public void create() {
        Restaurant created = restaurantService.create(getCreated());
        Restaurant newRest = getCreated();
        newRest.setId(created.getId());
        RESTAURANT_MATCHER.assertMatch(created, newRest);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(created.getId()), newRest);
    }

    @Test
    public void createDuplicateName() {
        assertThrows(DataAccessException.class, () -> restaurantService.create(new Restaurant("Тануки", "дубликат Тануки")));
    }

    @Test
    public void update() {
        restaurantService.update(getUpdated());
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(START_SEQ_REST), getUpdated());
    }

    @Test
    public void delete() {
        restaurantService.delete(START_SEQ_REST);
        assertThrows(NotFoundException.class, () -> restaurantService.get(START_SEQ_REST));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(10));
    }

    @Test
    public void getAllWithRating() {
    }
}