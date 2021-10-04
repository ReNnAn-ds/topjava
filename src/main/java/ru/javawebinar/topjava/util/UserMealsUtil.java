package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 600),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 8, 30), "Завтрак", 2500)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayOfMonth = new HashMap<>();

        LocalDate currentDate = meals.get(0).getDateTime().toLocalDate();
        int caloriesSumPerDay = 0;
        for (int a = 0; a < meals.size(); a++) {
            UserMeal userMeal = meals.get(a);
            if (!userMeal.getDateTime().toLocalDate().equals(currentDate)) {
                caloriesPerDayOfMonth.put(currentDate, caloriesSumPerDay);
                currentDate = userMeal.getDateTime().toLocalDate();
                caloriesSumPerDay = 0;
            }
            caloriesSumPerDay = caloriesSumPerDay + userMeal.getCalories();

            if (a == meals.size() - 1){
                caloriesPerDayOfMonth.put(currentDate, caloriesSumPerDay);
            }
        }

        List<UserMealWithExcess> filteredUserMealsWithExcess = new ArrayList<>();

        for (int a = 0; a < meals.size(); a++) {
            UserMeal userMeal = meals.get(a);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredUserMealsWithExcess.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), caloriesPerDayOfMonth.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) > caloriesPerDay));
            }
        }

        return filteredUserMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
