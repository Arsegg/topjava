package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final List<MealTo> MEAL_TOS;
    static {
        log.debug("Setting seed...");
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        log.debug("Seed is set.");

        log.debug("Generating mealTos...");
        MEAL_TOS = Stream.generate(() -> new MealTo(LocalDateTime.ofEpochSecond(random.nextInt(), random.nextInt(1_000_000_000), ZoneOffset.UTC),
                        "Description #" + random.nextInt(1, Integer.MAX_VALUE),
                        random.nextInt(0, Integer.MAX_VALUE),
                        random.nextBoolean()))
                .limit(1_000)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        log.debug("MealTos is generated.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Setting meals attribute...");
        request.setAttribute("meals", MEAL_TOS);
        log.debug("Meals attribute is set.");

        log.debug("Forwarding to meals...");
        final RequestDispatcher meals = request.getRequestDispatcher("meals.jsp");
        meals.forward(request, response);
        log.debug("Forwarded to meals.");
    }
}
