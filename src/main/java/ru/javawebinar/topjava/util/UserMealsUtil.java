package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealAggregate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil {
    public static void main(String[] args) {
        final List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 30, 13, 0), "Обед", 1_000),
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 31, 10, 0), "Завтрак", 1_000),
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2_020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        final UserMealAggregate mealAggregate = new UserMealAggregate(meals);
        final List<UserMeal> userMeals = mealAggregate.filteredByStreams(LocalTime.of(7, 0), LocalTime.of(12, 0));
        userMeals.stream()
                .map(userMeal -> new SimpleEntry<>(userMeal, mealAggregate.isExceeded(userMeal, 2_000)))
                .forEach(System.out::println);
    }
}
