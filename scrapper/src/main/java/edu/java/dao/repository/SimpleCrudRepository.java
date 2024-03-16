package edu.java.dao.repository;

import org.springframework.dao.DataAccessException;
import java.util.List;

public interface SimpleCrudRepository<T, V> {
    void add(V object) throws DataAccessException;

    void remove(T id) throws DataAccessException;

    List<V> findAll();
}
