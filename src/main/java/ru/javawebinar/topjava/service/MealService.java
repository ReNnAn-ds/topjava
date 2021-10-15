package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public List<Meal> getAll(int userId) {
        return new ArrayList<>(repository.getAll(userId));
    }

    public Meal get(int id, int userId) {
        return repository.get(id, userId);
    }

    public Meal save(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        repository.delete(id, userId);

    }
}