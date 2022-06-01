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

    public UserMealAggregate(List<UserMeal> userMealList) {
        userMealPerDay = userMealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getLocalDate,
                        Collectors.collectingAndThen(Collectors.toList(),
                                userMeals -> new SimpleEntry<>(userMeals,
                                        userMeals.stream()
                                                .mapToInt(UserMeal::getCalories)
                                                .sum()))));
    }

    public List<UserMeal> filteredByStreams(LocalTime startTime, LocalTime endTime) {
        return userMealPerDay.entrySet()
                .stream()
                .flatMap(localDateSimpleEntryEntry -> localDateSimpleEntryEntry.getValue().getKey()
                        .stream()
                        .filter(userMeal -> userMeal.isBetweenHalfOpen(startTime, endTime)))
                .collect(Collectors.toList());
    }

    public boolean isExceeded(UserMeal userMeal, int caloriesPerDay) {
        return userMealPerDay.get(userMeal.getLocalDate()).getValue() > caloriesPerDay;
    }
}
