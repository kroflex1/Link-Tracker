package edu.java.dao.repository;

import edu.java.dao.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.util.List;

public interface LinkRepository extends SimpleCrudRepository<URI, Link> {
    Link get(URI url);
    List<Link> findAllOutdatedLinks(Duration duration);
}
