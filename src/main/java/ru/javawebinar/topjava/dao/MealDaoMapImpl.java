package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealDaoMapImpl implements GenericDao<Meal, Integer> {
    private final static Map<Integer, Meal> mealsRepository = new ConcurrentHashMap<>();
    private final static AtomicInteger id = new AtomicInteger(0);

    @Override
    public void create(Meal meal) {
        if (mealsRepository.containsKey(meal.getId())) {
            throw new RuntimeException("PrimaryKey ["+meal.getId()+"] already exists");
        }
        meal.setId(getNextId());
        mealsRepository.put(meal.getId(), meal);
    }

    private int getNextId() {
        return id.incrementAndGet();
    }

    public void delete(int id) {
        if (!mealsRepository.containsKey(id)) {
            throw new RuntimeException("PrimaryKey [" + id + "] doesn't exists");
        }
        mealsRepository.remove(id);
    }

    public MealDaoMapImpl() {
    }

    @Override
    public void delete(Meal meal) {
        delete(meal.getId());

    }

    @Override
    public Meal read(Integer id) {

        return mealsRepository.get(id);
    }

    @Override
    public void update(Meal meal) {
        if (!mealsRepository.containsKey(meal.getId())) {
            throw new RuntimeException("PrimaryKey ["+meal.getId()+"] doesn't exists");
        }
        mealsRepository.put(meal.getId(), meal);
    }

    public List<MealWithExceed> getAllMeals() {
        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(mealsRepository.values().stream().collect(Collectors.toList()), LocalTime.MIN, LocalTime.MAX, 2000);
        return meals;
    }
}
