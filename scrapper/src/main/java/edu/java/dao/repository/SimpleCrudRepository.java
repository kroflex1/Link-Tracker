package edu.java.dao.repository;

import edu.java.exceptions.AlreadyRegisteredDataException;
import java.util.List;

public interface SimpleCrudRepository<T, V> {
    void add(V object) throws AlreadyRegisteredDataException;

    void remove(T id) throws IllegalArgumentException;

    List<V> findAll();
}
