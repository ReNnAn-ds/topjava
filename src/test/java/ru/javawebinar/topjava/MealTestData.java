package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final int LOGGED_USER_ID = SecurityUtil.authUserId();

    public static final int JANUARY_BREAKFAST_ID = START_SEQ + 2;
    public static final int JANUARY_DINNER_ID = START_SEQ + 3;
    public static final int JANUARY_SUPPER_ID = START_SEQ + 4;
    public static final int FEBRUARY_BREAKFAST_ID = START_SEQ + 5;
    public static final int FEBRUARY_DINNER_ID = START_SEQ + 6;
    public static final int FEBRUARY_SUPPER_ID = START_SEQ + 7;
    public static final int STRANGER_MEAL_ID = START_SEQ + 10;
    public static final int NOT_FOUND_ID = 40;

    public static final Meal JANUARY_BREAKFAST = new Meal(JANUARY_BREAKFAST_ID, LocalDateTime.parse("2020-01-30 10:00:00", DATE_TIME_FORMATTER), "Завтрак", 500);
    public static final Meal JANUARY_DINNER = new Meal(JANUARY_DINNER_ID, LocalDateTime.parse("2020-01-30 13:00:00", DATE_TIME_FORMATTER), "Обед", 1000);
    public static final Meal JANUARY_SUPPER = new Meal(JANUARY_SUPPER_ID, LocalDateTime.parse("2020-01-30 20:00:00", DATE_TIME_FORMATTER), "Ужин", 500);
    public static final Meal FEBRUARY_BREAKFAST = new Meal(FEBRUARY_BREAKFAST_ID, LocalDateTime.parse("2020-02-01 09:00:00", DATE_TIME_FORMATTER), "Завтрак", 500);
    public static final Meal FEBRUARY_DINNER = new Meal(FEBRUARY_DINNER_ID, LocalDateTime.parse("2020-02-01 13:30:00", DATE_TIME_FORMATTER), "Обед", 1500);
    public static final Meal FEBRUARY_SUPPER = new Meal(FEBRUARY_SUPPER_ID, LocalDateTime.parse("2020-02-01 19:00:00", DATE_TIME_FORMATTER), "Ужин", 800);
    public static final Meal STRANGER_MEAL = new Meal(STRANGER_MEAL_ID, LocalDateTime.parse("2020-01-31 13:00:00", DATE_TIME_FORMATTER), "Обед", 500);

    public static Meal getNew(){
        return new Meal(null, LocalDateTime.of(2021, Month.OCTOBER, 25, 13, 30), "Перекус", 1500);
    }

    public static Meal getNewDuplicate(){
        return new Meal(null, LocalDateTime.parse("2020-01-30 13:00:00", DATE_TIME_FORMATTER), "Нямка", 1000);
    }

    public static Meal getUpdated(){
        Meal updated = new Meal(JANUARY_BREAKFAST);
        updated.setDateTime(LocalDateTime.of(2021, Month.OCTOBER, 26, 12, 30));
        updated.setDescription("Мак");
        updated.setCalories(1200);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }


}
