package edu.java.dao.repository;

import edu.java.dao.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.util.List;

public interface LinkRepository extends SimpleCrudRepository<URI, Link> {
    List<Link> findAllOutdatedLinks(Duration duration);
}
