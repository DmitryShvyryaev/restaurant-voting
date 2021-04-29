package ru.topjava.lunchvote.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.testdata.UserTestData;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.DishTestData.DISH_MATCHER;
import static ru.topjava.lunchvote.testdata.DishTestData.getCreated;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;

class DishControllerTest extends AbstractControllerTest {

    private final static String REST_URL = "/rest/restaurants/" + START_SEQ_REST + "/dishes/";
    @Autowired
    private JsonConverter jsonConverter;

    @Test
    void getAll() throws Exception {
        List<Dish> expectedMenu = populateMenu(START_SEQ_REST, 5);

        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Dish> actualMenu = jsonConverter.readValuesFromJson(result, Dish.class);
        DISH_MATCHER.assertMatch(actualMenu, expectedMenu);
    }

    @Test
    void get() throws Exception {
        Dish created = dishService.create(START_SEQ_REST, getCreated());
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + created.id())
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Dish actual = jsonConverter.readValueFromJson(result, Dish.class);
        DISH_MATCHER.assertMatch(actual, created);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}