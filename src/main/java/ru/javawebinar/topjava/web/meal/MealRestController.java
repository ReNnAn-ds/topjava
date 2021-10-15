package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<Meal> getAll(int userId) {
        log.info("get all");
        return service.getAll(userId);
    }

    public Meal get(int id, int userId) {
        log.info("get meal with id={}", id);
        return service.get(id, userId);
    }

    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        return service.save(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete meal with id={}", id);
        service.delete(id, userId);
    }

}