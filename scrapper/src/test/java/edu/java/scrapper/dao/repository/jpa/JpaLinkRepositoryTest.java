package edu.java.scrapper.dao.repository.jpa;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.repository.jpa.entity.Link;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class JpaLinkRepositoryTest extends IntegrationTest {

    @Autowired JpaLinkRepository jpaLinkRepository;

    @Test
    @Rollback
    void testGetOutdatedLinks() {
        int numberOfFreshLinks = 2;
        int numberOfOldLinks = 3;
        OffsetDateTime outdated = OffsetDateTime.now().minusDays(1);
        List<Link> expected = new ArrayList<>();
        for (int i = 0; i < numberOfFreshLinks; i++) {
            jpaLinkRepository.save(new Link(
                "http://fresh" + i,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
            ));
        }
        for (int i = 0; i < numberOfOldLinks; i++) {
            Link oldLink = new Link(
                "http://old" + i,
                OffsetDateTime.now(),
                outdated,
                OffsetDateTime.now()
            );
            expected.add(oldLink);
            jpaLinkRepository.save(oldLink);
        }

        List<Link> actual = jpaLinkRepository.findAllByLastCheckTimeLessThanEqual(outdated);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.stream().map(Link::getLink).toList(), actual.stream().map(Link::getLink).toList());
    }
}
