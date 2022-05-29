package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        final List<UserMeal> userMeals = new ArrayList<>();
        final Map<LocalDate, Integer> caloriesMap = new HashMap<>();
        for (final UserMeal meal : meals) {
            final LocalDateTime dateTime = meal.getDateTime();
            final LocalDate localDate = dateTime.toLocalDate();
            final int calories = meal.getCalories();
            caloriesMap.merge(localDate, calories, Integer::sum);
            final LocalTime localTime = dateTime.toLocalTime();
            if (!TimeUtil.isBetweenHalfOpen(localTime, startTime, endTime)) {
                continue;
            }
            userMeals.add(meal);
        }
        final List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>(userMeals.size());
        for (final UserMeal userMeal : userMeals) {
            final LocalDateTime dateTime = userMeal.getDateTime();
            final LocalDate localDate = dateTime.toLocalDate();
            final boolean isExceeded = caloriesMap.get(localDate) > caloriesPerDay;
            final String description = userMeal.getDescription();
            final int calories = userMeal.getCalories();
            final UserMealWithExcess userMealWithExcess = new UserMealWithExcess(dateTime, description, calories, isExceeded);
            userMealWithExcesses.add(userMealWithExcess);
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        final Map<LocalDate, Integer> map = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    final LocalDateTime dateTime = userMeal.getDateTime();
                    return new UserMealWithExcess(dateTime, userMeal.getDescription(), userMeal.getCalories(), map.get(dateTime.toLocalDate()) > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }
}
