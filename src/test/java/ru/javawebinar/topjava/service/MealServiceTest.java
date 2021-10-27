package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(FEBRUARY_BREAKFAST_ID, LOGGED_USER_ID);
        assertMatch(meal, FEBRUARY_BREAKFAST);
    }

    @Test
    public void getNotFound(){
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND_ID, LOGGED_USER_ID));
    }

    @Test
    public void getStranger(){
        assertThrows(NotFoundException.class, () -> mealService.get(STRANGER_MEAL_ID, LOGGED_USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(JANUARY_DINNER_ID, LOGGED_USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(JANUARY_DINNER_ID, LOGGED_USER_ID));
    }

    @Test
    public void deleteStranger(){
        assertThrows(NotFoundException.class, () -> mealService.delete(STRANGER_MEAL_ID, LOGGED_USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = mealService.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 31), LOGGED_USER_ID);
        assertMatch(meals, JANUARY_SUPPER, JANUARY_DINNER, JANUARY_BREAKFAST);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(LOGGED_USER_ID);
        assertMatch(meals, FEBRUARY_SUPPER, FEBRUARY_DINNER, FEBRUARY_BREAKFAST, JANUARY_SUPPER, JANUARY_DINNER, JANUARY_BREAKFAST);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, LOGGED_USER_ID);
        assertMatch(mealService.get(JANUARY_BREAKFAST_ID, LOGGED_USER_ID), getUpdated());
    }

    @Test
    public void updateStranger(){
        Meal updated = STRANGER_MEAL;
        updated.setDescription("Комбикорм");
        assertThrows(NotFoundException.class, () -> mealService.update(updated, LOGGED_USER_ID));
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), LOGGED_USER_ID);
        Integer id = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(id);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(id, LOGGED_USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate(){
        assertThrows(DataAccessException.class, () -> mealService.create(getNewDuplicate(), LOGGED_USER_ID));
    }
}