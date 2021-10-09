package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {

    private static final int CALORIES_PER_DAY = 2000;

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealTo> filteredMeals = MealsUtil.filteredByStreams(MealsUtil.createMealsList(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        req.setAttribute("mealsList", filteredMeals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
