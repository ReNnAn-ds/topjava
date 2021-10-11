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

public class MealServlet extends HttpServlet {

    private static final int CALORIES_PER_DAY = 2000;

    private static final String INSERT_OR_EDIT = "/mealForm.jsp";

    private static final String LIST_USER = "/meals.jsp";

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealDao dao;

    public MealServlet() {
        log.trace("MealServlet constructor call");
        dao = new MealDao();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward;
        int id;
        String actionParam = req.getParameter("action");
        String action = actionParam == null || actionParam.isEmpty() ? "default" : actionParam;
        switch (action.toLowerCase()) {
            case "add":
                log.trace("servlet {} forward to mealForm.jsp", MealServlet.class.getSimpleName());
                forward = INSERT_OR_EDIT;
                Meal newMeal = new Meal(0, LocalDateTime.now(), "Описание", 1000);
                req.setAttribute("meal", newMeal);
                break;
            case "edit":
                forward = INSERT_OR_EDIT;
                id = Integer.parseInt(req.getParameter("mealId"));
                log.trace("servlet {} forward to mealForm.jsp with id={}", MealServlet.class.getSimpleName(), id);
                Meal currentMeal = dao.getById(id);
                req.setAttribute("meal", currentMeal);
                break;
            case "delete":
                id = Integer.parseInt(req.getParameter("mealId"));
                log.trace("servlet {} delete meal with id={}", MealServlet.class.getSimpleName(), id);
                dao.delete(id);
            case "default":
            default:
                log.trace("servlet {} show meals", MealServlet.class.getSimpleName());
                forward = LIST_USER;
                req.setAttribute("mealsList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("mealId"));
        log.trace("servlet {} {} meal {}", MealServlet.class.getSimpleName(), id == 0 ? "add" : "edit", id == 0 ? "" : "with id=" + id);
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        dao.add(id, dateTime, description, calories);

        req.setAttribute("mealsList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        req.getRequestDispatcher(LIST_USER).forward(req, resp);

    }
}
