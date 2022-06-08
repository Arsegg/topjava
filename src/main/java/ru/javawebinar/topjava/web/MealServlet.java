package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2_000;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Setting attributes...");
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        request.setAttribute("meals", MealsUtil.filteredByStreams(MealsUtil.MEALS,
                LocalTime.MIN,
                LocalTime.MAX,
                CALORIES_PER_DAY));
        log.debug("Attributes are set.");

        log.debug("Forwarding to meals...");
        final RequestDispatcher meals = request.getRequestDispatcher("meals.jsp");
        meals.forward(request, response);
        log.debug("Forwarded to meals.");
    }
}
