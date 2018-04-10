package ru.javawebinar.topjava.dao;

import java.io.Serializable;

public interface GenericDao<T, PK extends Serializable> {
    void create(T t);
    void delete(T t);
    T read(PK id);
    void update(T t);
}
