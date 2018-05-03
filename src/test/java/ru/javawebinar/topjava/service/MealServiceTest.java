package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {


    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal mealForAdmin = service.get(100002, ADMIN_ID);
        assertMatch(mealForAdmin, ADMIN_MEAL1);
        Meal mealForUser = service.get(100004, USER_ID);
        assertMatch(mealForUser, USER_MEAL1);

    }

    @Test
    public void delete() {
        service.delete(100002, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL2);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(service.getBetweenDates(LocalDate.of(2015, 5, 1),
                LocalDate.of(2015, 7, 1), USER_ID), USER_MEAL2, USER_MEAL1);
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, 6, 1, 9, 0),
                LocalDateTime.of(2015, 6, 1, 11, 0), USER_ID), USER_MEAL1);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL2, ADMIN_MEAL1);
        assertMatch(service.getAll(USER_ID), USER_MEAL2, USER_MEAL1);
    }

    @Test
    public void update() {
        Meal updatedForUser = new Meal(USER_MEAL1);
        updatedForUser.setDescription("Лёгкий завтрак");
        updatedForUser.setCalories(200);
        AuthorizedUser.setId(USER_ID);
        service.update(updatedForUser, USER_ID);
        assertMatch(service.get(USER_MEAL1.getId(), USER_ID), updatedForUser);
        Meal updatedForAdmin = new Meal(ADMIN_MEAL2);
        updatedForAdmin.setDescription("Админский ужин");
        updatedForAdmin.setCalories(400);
        AuthorizedUser.setId(ADMIN_ID);
        service.update(updatedForAdmin, ADMIN_ID);
        assertMatch(service.get(ADMIN_MEAL2.getId(), ADMIN_ID), updatedForAdmin);
    }
    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(100002, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete() throws Exception {
        service.delete(100002, USER_ID);
    }


    @Test(expected = DataAccessException.class)
    public void duplicateDateCreate() throws Exception {
        AuthorizedUser.setId(USER_ID);
        service.create(new Meal(USER_MEAL1.getDateTime(), "Роллы", 200), AuthorizedUser.id());
    }
    @Test
    public void create() {
        Meal newMealForAdmin = new Meal(LocalDateTime.of(2015, 6, 1, 17, 0), "Админ обед", 1500);
        Meal newMealForUser = new Meal(LocalDateTime.of(2015, 6, 1, 15, 0), "Обед", 1300);
         AuthorizedUser.setId(USER_ID);
        Meal createdMealForUser = service.create(newMealForUser, AuthorizedUser.id());
        newMealForUser.setId(createdMealForUser.getId());
        assertMatch(service.getAll(USER_ID), USER_MEAL2, newMealForUser, USER_MEAL1);
        AuthorizedUser.setId(ADMIN_ID);
        Meal createdMealForAdmin = service.create(newMealForAdmin, AuthorizedUser.id());
        newMealForUser.setId(createdMealForAdmin.getId());
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL2, newMealForAdmin, ADMIN_MEAL1);

    }
}