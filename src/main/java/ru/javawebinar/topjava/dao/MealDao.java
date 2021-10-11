package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MealDao {

    private static final Logger log = LoggerFactory.getLogger(MealDao.class);

    public static AtomicInteger mealId = new AtomicInteger(1);

    private ConcurrentMap<Integer, Meal> mealRepository;

    {
        log.debug("repository init with default values");
        mealRepository = MealsUtil.createMealsList().stream()
                .collect(Collectors.toConcurrentMap(Meal::getMealId, Function.identity()));
    }


    public void add(int id, LocalDateTime dateTime, String description, int calories) {

        Meal meal = new Meal(id == 0 ? mealId.getAndIncrement() : id, dateTime, description, calories);

        mealRepository.merge(meal.getMealId(), meal, (oldValue, newValue) -> newValue);
    }

    public void delete(int mealId) {
        mealRepository.remove(mealId);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(mealRepository.values());
    }

    public Meal getById(int mealId) {
        return mealRepository.get(mealId);
    }


}
