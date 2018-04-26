package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    @Override
    public void delete(int userId, int id) throws NotFoundException {
        if (!repository.delete(userId, id)) {
            throw new NotFoundException("Not found either user or meal: " + "userId = " + userId + " mealId = " + id );
        }
    }

    @Override
    public Meal get(int userId, int id) throws NotFoundException {
        Meal meal = repository.get(userId, id);
        if (meal == null) {
            throw new NotFoundException("Not found either user or meal: " + "userId = " + userId + " mealId = " + id );
        }
        return meal;
    }

    @Override
    public void update(int userId, Meal meal) {
        Meal m = repository.save(userId, meal);
        if (m == null) {
            throw new NotFoundException("Not found either user or meal: " + "userId = " + userId + " mealId = " + meal.getId() );
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }
}