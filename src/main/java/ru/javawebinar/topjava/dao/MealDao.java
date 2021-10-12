package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    void add(int id, LocalDateTime dateTime, String description, int calories);

    void delete(int id);

    List<Meal> getAll();

    Meal getById(int id);
}
