package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public List<MealTo> getFiltered(int userId, String fromDate, String toDate, String fromTime, String toTime) {

        Collection<Meal> meals;
        if (StringUtils.hasLength(fromDate) || StringUtils.hasLength(toDate)) {
            meals = repository.getFiltered(userId,
                    StringUtils.hasLength(fromDate) ? LocalDate.parse(fromDate) : LocalDate.MIN,
                    StringUtils.hasLength(toDate) ? LocalDate.parse(toDate) : LocalDate.MAX);
        } else {
            meals = repository.getAll(userId);
        }

        List<MealTo> mealTos;
        if (StringUtils.hasLength(fromTime) || StringUtils.hasLength(toTime)) {
            mealTos = MealsUtil.getFilteredTos(meals,
                    SecurityUtil.authUserCaloriesPerDay(),
                    StringUtils.hasLength(fromTime) ? LocalTime.parse(fromTime) : LocalTime.MIN,
                    StringUtils.hasLength(toTime) ? LocalTime.parse(toTime) : LocalTime.MAX);
        } else {
            mealTos = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        }

        return mealTos;
    }

    public List<MealTo> getAll(int userId) {
        return MealsUtil.getTos(repository.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public Meal save(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);

    }
}