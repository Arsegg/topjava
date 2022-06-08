package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2_000;
    private static final List<MealTo> MEAL_TOS = MealsUtil.filteredByStreams(MealsUtil.MEALS,
            LocalTime.MIN,
            LocalTime.MAX,
            CALORIES_PER_DAY);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Setting attributes...");
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        request.setAttribute("meals", MEAL_TOS);
        log.debug("Attributes are set.");

        log.debug("Forwarding to meals...");
        final RequestDispatcher meals = request.getRequestDispatcher("meals.jsp");
        meals.forward(request, response);
        log.debug("Forwarded to meals.");
    }
}
