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

public class MealDaoImpl implements MealDao {

    private static final Logger log = LoggerFactory.getLogger(MealDaoImpl.class);

    private static AtomicInteger mealId = new AtomicInteger(1);

    private ConcurrentMap<Integer, Meal> mealRepository;

    {
        log.trace("repository init with default values");
        mealRepository = MealsUtil.createMealsList().stream()
                .map(meal -> new Meal(mealId.getAndIncrement(), meal.getDateTime(), meal.getDescription(), meal.getCalories()))
                .collect(Collectors.toConcurrentMap(Meal::getMealId, Function.identity()));
    }


    @Override
    public void add(int id, LocalDateTime dateTime, String description, int calories) {
        int validId = id == 0 ? mealId.getAndIncrement() : id;
        log.debug("add/update meal with id={}", validId);

        Meal meal = new Meal(validId, dateTime, description, calories);

        mealRepository.merge(meal.getMealId(), meal, (oldValue, newValue) -> newValue);
    }

    @Override
    public void delete(int id) {
        log.debug("delete meal with id={}", id);
        mealRepository.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealRepository.values());
    }

    @Override
    public Meal getById(int id) {
        return mealRepository.get(id);
    }


}
