package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class MealServlet extends HttpServlet {

    private static final int CALORIES_PER_DAY = 2000;

    private static final String INSERT_OR_EDIT = "/mealForm.jsp";

    private static final String LIST_USER = "/meals.jsp";

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private static AtomicInteger mealId = new AtomicInteger(1);

    private MealDao dao;

    public MealServlet() {
        log.debug("MealServlet constructor call");
        dao = new MealDao();
    }

    public static AtomicInteger getMealId() {
        return mealId;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward;
        int id;
        String actionParam = req.getParameter("action");
        String action = actionParam == null ? "default" : actionParam;
        switch (action.toLowerCase()) {
            case "add":
                log.debug("add doGet");
                forward = INSERT_OR_EDIT;
                Meal newMeal = new Meal(mealId.getAndIncrement(), LocalDateTime.now(), "Описание", 1000);
                req.setAttribute("meal", newMeal);
                break;
            case "edit":
                log.debug("edit");
                forward = INSERT_OR_EDIT;
                id = Integer.parseInt(req.getParameter("mealId"));
                Meal currentMeal = dao.getById(id);
                req.setAttribute("meal", currentMeal);
                break;
            case "delete":
                log.debug("delete");
                id = Integer.parseInt(req.getParameter("mealId"));
                dao.delete(id);
            case "default":
            default:
                log.debug("show meals");
                forward = LIST_USER;
                req.setAttribute("mealsList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("add/update doPost");
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("mealId"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        Meal meal = new Meal(id, dateTime, description, calories);

        dao.add(meal);

        req.setAttribute("mealsList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        req.getRequestDispatcher(LIST_USER).forward(req, resp);

    }
}
