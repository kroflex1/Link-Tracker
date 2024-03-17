package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.Chat;
import edu.java.dao.dto.Link;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Transactional
    @Rollback
    void testAddNewLink() {
        Link link =
            new Link(URI.create("http://link"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(link);

        List<Link> links = jdbcLinkRepository.findAll();

        assertEquals(link, links.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllLinks() {
        int numberOfLinks = 5;
        List<Link> expected = new ArrayList<>();
        for (int i = 1; i <= numberOfLinks; i++) {
            Link newLink =
                new Link(URI.create("http://" + i), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
            expected.add(newLink);
            jdbcLinkRepository.add(newLink);
        }

        List<Link> actual = jdbcLinkRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllOutdatedLinks() {
        int numberOfLinks = 4;
        int numberOfOldLinks = 3;
        for (int i = 1; i <= numberOfLinks; i++) {
            OffsetDateTime date = OffsetDateTime.now();
            Link newLink = new Link(URI.create("http://" + i), date, date, date);
            jdbcLinkRepository.add(newLink);
        }
        List<Link> expected = new ArrayList<>();
        for (int i = 1; i <= numberOfOldLinks; i++) {
            OffsetDateTime date = OffsetDateTime.now().minusDays(2);
            Link newLink = new Link(URI.create("http://" + (numberOfLinks + i)), date, date, date);
            expected.add(newLink);
            jdbcLinkRepository.add(newLink);
        }

        List<Link> actual = jdbcLinkRepository.findAllOutdatedLinks(Duration.ofDays(1));

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveLink() {
        Link firstLink =
            new Link(URI.create("http://" + 1), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        Link secondLink =
            new Link(URI.create("http://" + 2), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(firstLink);
        jdbcLinkRepository.add(secondLink);

        List<Link> links = jdbcLinkRepository.findAll();
        assertEquals(2, links.size());
        jdbcLinkRepository.remove(URI.create("http://" + 1));
        assertEquals(List.of(secondLink), jdbcLinkRepository.findAll());
    }

    @Test
    @Transactional
    @Rollback
    void testAddAlreadyExistingLink() {
        Link link = new Link(URI.create("http://1"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(link);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            jdbcLinkRepository.add(link));
        assertEquals("http://1 has already been registered", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteNonExistentChat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            jdbcLinkRepository.remove(URI.create("http://sonelink")));
        assertEquals("http://sonelink cannot be deleted because it wasn`t found", exception.getMessage());
    }
}
