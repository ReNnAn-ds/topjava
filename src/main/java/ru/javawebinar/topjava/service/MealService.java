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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public List<MealTo> getFiltered(int userId, String fromDate, String toDate, String fromTime, String toTime) {

        List<MealTo> mealTos = MealsUtil.getFilteredTos(repository.getAll(userId),
                SecurityUtil.authUserCaloriesPerDay(),
                StringUtils.hasLength(fromTime) ? LocalTime.parse(fromTime) : LocalTime.MIN,
                StringUtils.hasLength(toTime) ? LocalTime.parse(toTime) : LocalTime.MAX);

        if (StringUtils.hasLength(fromDate) || StringUtils.hasLength(toDate)) {
            Stream<MealTo> mealToStream = mealTos.stream();
            if (StringUtils.hasLength(fromDate)) {
                mealToStream = mealToStream.filter(mealTo -> mealTo.getDateTime().toLocalDate().isAfter(LocalDate.parse(fromDate)));
            }
            if (StringUtils.hasLength(toDate)) {
                mealToStream = mealToStream.filter(mealTo -> mealTo.getDateTime().toLocalDate().isBefore(LocalDate.parse(toDate)));
            }
            mealTos = mealToStream.collect(Collectors.toList());
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