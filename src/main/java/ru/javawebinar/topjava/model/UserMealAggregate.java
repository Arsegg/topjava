package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class UserMealAggregate {
    private final Map<LocalDate, Entry<List<UserMeal>, Integer>> userMealPerDay;
    private final int caloriesPerDay;

    public UserMealAggregate(List<UserMeal> userMealList, int caloriesPerDay) {
        userMealPerDay = userMealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getLocalDate,
                        Collectors.collectingAndThen(Collectors.toList(),
                                userMeals -> new SimpleEntry<>(userMeals,
                                        userMeals.stream()
                                                .mapToInt(UserMeal::getCalories)
                                                .sum()))));
        this.caloriesPerDay = caloriesPerDay;
    }

    public List<UserMeal> filteredByStreams(LocalTime startTime, LocalTime endTime) {
        return userMealPerDay.entrySet()
                .stream()
                .flatMap(localDateSimpleEntryEntry -> localDateSimpleEntryEntry.getValue().getKey()
                        .stream()
                        .filter(userMeal -> userMeal.isBetweenHalfOpen(startTime, endTime)))
                .map(userMeal -> new UserMeal(userMeal, isExceeded(userMeal)))
                .collect(Collectors.toList());
    }

    private boolean isExceeded(UserMeal userMeal) {
        return userMealPerDay.get(userMeal.getLocalDate()).getValue() > caloriesPerDay;
    }
}
