package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, List<Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        int dice = 0;
        for (Meal meal : MealsUtil.MEALS) {
            save(dice++ % 2 == 0 ? 1 : 2, meal);
        }
    }

    @Override
    public Meal save(int userId, Meal meal) {
        List<Meal> mealsPerUser = repository.get(userId);
        if (mealsPerUser == null) {
            mealsPerUser = new ArrayList<>();
            meal.setId(counter.incrementAndGet());
            mealsPerUser.add(meal);
            repository.put(userId, mealsPerUser);
            return meal;
        }

        mealsPerUser = new ArrayList<>(mealsPerUser);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsPerUser.add(meal);
            repository.put(userId, mealsPerUser);
            return meal;
        }

        Meal mealToUpdate = mealsPerUser.stream().filter(m -> m.getId() == meal.getId()).findFirst().orElse(null);
        if (meal == null) {
            return meal;
        }
        mealsPerUser.set(mealsPerUser.indexOf(mealToUpdate), meal);
        repository.put(userId, mealsPerUser);
        return meal;

    }

    @Override
    public boolean delete(int userId, int id) {
        List<Meal> mealsPerUser = repository.get(userId);
        if (mealsPerUser == null) {
            return false;
        }
        Meal mealToDelete = mealsPerUser.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
        if (mealToDelete == null) {
            return false;
        }
        mealsPerUser.remove(mealToDelete);
        repository.put(userId, mealsPerUser);
        return true;
    }

    @Override
    public Meal get(int userId, int id) {
        List<Meal> mealsPerUser = repository.get(userId);
        if (mealsPerUser == null) {
            return null;
        }
        Meal mealToGet = mealsPerUser.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
        return mealToGet;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        List<Meal> mealsPerUser = repository.get(userId);
        if (mealsPerUser == null) {
            return Collections.emptyList();
        }

        return mealsPerUser.stream().sorted(Comparator.comparing(Meal::getDate).reversed()).collect(Collectors.toList());
    }
}

