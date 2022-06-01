package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserMeal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    LocalDateTime getDateTime() {
        return dateTime;
    }

    LocalDate getLocalDate() {
        return getDateTime().toLocalDate();
    }

    LocalTime getLocalTime() {
        return getDateTime().toLocalTime();
    }

    int getCalories() {
        return calories;
    }

    boolean isBetweenHalfOpen(LocalTime startTime, LocalTime endTime) {
        final LocalTime lt = getLocalTime();
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
