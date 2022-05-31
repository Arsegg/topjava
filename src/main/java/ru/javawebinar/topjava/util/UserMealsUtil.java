package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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
        System.out.println(filteredByStreams2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> sumOfCaloriesPerDay = new HashMap<>();
        for (final UserMeal meal : meals) {
            sumOfCaloriesPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        final List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (final UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), sumOfCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> sumOfCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    final LocalDateTime dateTime = userMeal.getDateTime();
                    return new UserMealWithExcess(dateTime, userMeal.getDescription(), userMeal.getCalories(), sumOfCaloriesPerDay.get(dateTime.toLocalDate()) > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                                Collectors.collectingAndThen(Collectors.toList(),
                                        userMeals -> new AbstractMap.SimpleEntry<>(userMeals,
                                                userMeals.stream()
                                                        .mapToInt(UserMeal::getCalories)
                                                        .sum()))),
                        localDateIntegerMap -> localDateIntegerMap.entrySet()
                                .stream()
                                .flatMap(localDateSimpleEntryEntry -> {
                                    final AbstractMap.SimpleEntry<List<UserMeal>, Integer> value = localDateSimpleEntryEntry.getValue();
                                    return value.getKey()
                                            .stream()
                                            .filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                                                    userMeal.getDateTime().toLocalTime(), startTime, endTime))
                                            .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                                                    userMeal.getDescription(), userMeal.getCalories(),
                                                    value.getValue() > caloriesPerDay));
                                })
                                .collect(Collectors.toList())
                ));
    }
}
