package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
        });
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Meal editedMeal = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal.getUserId() == userId ? meal : oldMeal);
        return editedMeal == null ? null : editedMeal.getUserId() != userId ? null : editedMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(id) == null || repository.get(id).getUserId() != userId) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal == null ? null : meal.getUserId() == userId ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFiltered(int userID, LocalDate fromDate, LocalDate toDate) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userID)
                .filter(meal -> DateTimeUtil.isBetweenDate(meal.getDate(), fromDate, toDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

