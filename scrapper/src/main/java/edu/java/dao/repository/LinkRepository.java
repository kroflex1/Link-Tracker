package edu.java.dao.repository;

import edu.java.dao.dto.LinkDTO;
import java.net.URI;
import java.time.Duration;
import java.util.List;

public interface LinkRepository extends SimpleCrudRepository<URI, LinkDTO> {
    LinkDTO get(URI url);

    List<LinkDTO> findAllOutdatedLinks(Duration duration);
}
