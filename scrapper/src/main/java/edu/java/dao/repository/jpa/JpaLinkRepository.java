package edu.java.dao.repository.jpa;

import edu.java.dao.repository.jpa.entity.Link;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.springframework.data.repository.CrudRepository;
import java.time.OffsetDateTime;
import java.util.List;

public interface JpaLinkRepository extends CrudRepository<Link, String> {
    List<Link> findAllByLastCheckTimeLessThanEqual(OffsetDateTime outdated);

}
