package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealTo> getFiltered(String fromDate, String toDate, String fromTime, String toTime) {
        log.info("get filtered");

        if (!StringUtils.hasLength(fromDate) && !StringUtils.hasLength(toDate) && !StringUtils.hasLength(fromTime) && !StringUtils.hasLength(toTime)) {
            return getAll();
        }

        return service.getFiltered(SecurityUtil.authUserId(), fromDate, toDate, fromTime, toTime);
    }

    public List<MealTo> getAll() {
        log.info("get all");
        return service.getAll(SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get meal with id={}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal save(Meal meal) {
        log.info("save {}", meal);
        return service.save(new Meal(meal.getId(), SecurityUtil.authUserId(), meal.getDateTime(), meal.getDescription(), meal.getCalories()), SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete meal with id={}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

}