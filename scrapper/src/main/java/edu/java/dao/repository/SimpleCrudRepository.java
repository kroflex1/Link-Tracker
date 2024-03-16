package edu.java.dao.repository;

import java.util.List;

public interface SimpleCrudRepository<T, V> {
    void add(V object);

    void remove(T id);

    List<V> findAll();
}
