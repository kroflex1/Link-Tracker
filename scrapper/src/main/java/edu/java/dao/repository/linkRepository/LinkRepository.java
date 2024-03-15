package edu.java.dao.repository.linkRepository;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.SimpleCrudRepository;
import java.net.URI;

public interface LinkRepository extends SimpleCrudRepository<URI, LinkDTO> {
}
