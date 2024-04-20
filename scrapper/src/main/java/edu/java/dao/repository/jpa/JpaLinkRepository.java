package edu.java.dao.repository.jpa;

import edu.java.dao.repository.jpa.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface JpaLinkRepository extends CrudRepository<Link, String> {
    List<Link> findAllByLastCheckTimeLessThanEqual(OffsetDateTime outdated);

}
