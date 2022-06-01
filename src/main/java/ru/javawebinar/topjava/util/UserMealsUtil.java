package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
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

        System.out.println(filteredByCycles2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreams2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals,
                                                            LocalTime startTime,
                                                            LocalTime endTime,
                                                            int caloriesPerDay) {
        final Map<LocalDate, Integer> sumOfCaloriesPerDay = new HashMap<>();
        for (final UserMeal meal : meals) {
            sumOfCaloriesPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        final List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (final UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        sumOfCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals,
                                                             LocalTime startTime,
                                                             LocalTime endTime,
                                                             int caloriesPerDay) {
        final Map<LocalDate, Integer> sumOfCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),
                        startTime,
                        endTime))
                .map(userMeal -> {
                    final LocalDateTime dateTime = userMeal.getDateTime();
                    return new UserMealWithExcess(dateTime,
                            userMeal.getDescription(),
                            userMeal.getCalories(),
                            sumOfCaloriesPerDay.get(dateTime.toLocalDate()) > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCycles2(List<UserMeal> meals,
                                                            LocalTime startTime,
                                                            LocalTime endTime,
                                                            int caloriesPerDay) {
        final List<UserMealWithExcess> result = new ArrayList<>();
        filteredByCycles2(meals.iterator(), new HashMap<>(), startTime, endTime, caloriesPerDay, result);
        return result;
    }

    private static void filteredByCycles2(Iterator<UserMeal> iterator,
                                          Map<LocalDate, Integer> sumOfCaloriesPerDay,
                                          LocalTime startTime,
                                          LocalTime endTime,
                                          int caloriesPerDay,
                                          List<UserMealWithExcess> result) {
        if (iterator.hasNext()) {
            final UserMeal userMeal = iterator.next();
            sumOfCaloriesPerDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
            filteredByCycles2(iterator, sumOfCaloriesPerDay, startTime, endTime, caloriesPerDay, result);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        sumOfCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals,
                                                              LocalTime startTime,
                                                              LocalTime endTime,
                                                              int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                                Collectors.collectingAndThen(Collectors.toList(),
                                        userMeals -> new SimpleEntry<>(userMeals,
                                                userMeals.stream()
                                                        .mapToInt(UserMeal::getCalories)
                                                        .sum()))),
                        localDateIntegerMap -> localDateIntegerMap.entrySet()
                                .stream()
                                .flatMap(localDateSimpleEntryEntry -> localDateSimpleEntryEntry.getValue().getKey()
                                        .stream()
                                        .filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                                                userMeal.getDateTime().toLocalTime(), startTime, endTime))
                                        .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                                                userMeal.getDescription(),
                                                userMeal.getCalories(),
                                                localDateSimpleEntryEntry.getValue().getValue() > caloriesPerDay)))
                                .collect(Collectors.toList())
                ));
    }
}
