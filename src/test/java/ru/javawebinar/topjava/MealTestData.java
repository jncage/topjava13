package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final Meal USER_MEAL1 = new Meal(100004, LocalDateTime.of(2015, 6, 1, 10, 0), "Завтрак", 500);
    public static final Meal USER_MEAL2 = new Meal(100005, LocalDateTime.of(2015, 6, 1, 19, 0), "Ужин", 1200);
    public static final Meal ADMIN_MEAL1 = new Meal(100002, LocalDateTime.of(2015, 6, 1, 14, 0), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL2 = new Meal(100003, LocalDateTime.of(2015, 6, 1, 21, 0), "Админ ужин", 1500);


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

}