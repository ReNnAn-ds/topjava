package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        meal.setUser(userRepository.getById(userId));
        if (!meal.isNew() && crudRepository.findByIdAndUserId(meal.id(), userId).isEmpty()) {
            return null;
        }
        return crudRepository.save(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {

        return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {

        return crudRepository.findByIdAndUserId(id, userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {

        return crudRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllByUserIdAndDateTimeBetween(startDateTime, endDateTime, userId);
    }
}
