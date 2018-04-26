package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MealService service;

    public List<MealWithExceed> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<MealWithExceed> list = getAll();
        LocalDate finalStartDate = startDate == null ? LocalDate.MIN : startDate;
        LocalDate finalEndDate = endDate == null ? LocalDate.MAX : endDate;
        LocalTime finalStartTime = startTime == null ? LocalTime.MIN : startTime;
        LocalTime finalEndTime = endTime == null ? LocalTime.MAX : endTime;
        return list.stream().filter(meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalDate(), finalStartDate, finalEndDate))
                     .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalTime(), finalStartTime, finalEndTime))
                     .collect(Collectors.toList());

    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.getId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(AuthorizedUser.getId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(AuthorizedUser.getId(), meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(AuthorizedUser.getId(), id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(AuthorizedUser.getId(), meal);
    }

}