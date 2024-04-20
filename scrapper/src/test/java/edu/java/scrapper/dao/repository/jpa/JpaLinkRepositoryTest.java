package edu.java.scrapper.dao.repository.jpa;

import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.repository.jpa.entity.Link;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class JpaLinkRepositoryTest extends IntegrationTest {

    @Autowired JpaLinkRepository jpaLinkRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Rollback
    void testGetOutdatedLinks() {
        int numberOfFreshLinks = 2;
        int numberOfOldLinks = 3;
        OffsetDateTime outdated = OffsetDateTime.now().minusDays(1);
        List<Link> freshLinks = generateLinksWithLastCheckTime(numberOfFreshLinks, OffsetDateTime.now(), "fresh");
        List<Link> expectedOldLinks =generateLinksWithLastCheckTime(numberOfOldLinks, outdated, "old");

        jpaLinkRepository.saveAll(freshLinks);
        jpaLinkRepository.saveAll(expectedOldLinks);
        List<Link> actual = jpaLinkRepository.findAllByLastCheckTimeLessThanEqual(outdated);

        assertEquals(expectedOldLinks.size(), actual.size());
        assertEquals(expectedOldLinks.stream().map(Link::getLink).toList(), actual.stream().map(Link::getLink).toList());
    }

    private List<Link> generateLinksWithLastCheckTime(
        int numberOfLinks,
        OffsetDateTime lastCheckTime,
        String linkPathPattern
    ) {
        List<Link> result = new ArrayList<>();
        for (int i = 0; i < numberOfLinks; i++) {
            result.add(new Link(
                "http://%s".formatted(linkPathPattern) + i,
                OffsetDateTime.now(),
                lastCheckTime,
                OffsetDateTime.now()
            ));
        }
        return result;
    }
}
