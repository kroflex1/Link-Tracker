package edu.java.dao.repository;

import java.util.List;

public interface SimpleCrudRepository<T, V> {
    void add(V object) throws IllegalArgumentException;

    void remove(T id) throws IllegalArgumentException;

    List<V> findAll();
}
