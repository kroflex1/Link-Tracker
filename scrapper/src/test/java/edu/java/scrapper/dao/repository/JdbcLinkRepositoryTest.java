package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.exceptions.AlreadyRegisteredLinkException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Transactional
    @Rollback
    void testAddNewLink() throws AlreadyRegisteredLinkException {
        LinkDTO link =
            new LinkDTO(URI.create("http://link"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(link);

        List<LinkDTO> links = jdbcLinkRepository.findAll();

        assertEquals(link, links.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllLinks() throws AlreadyRegisteredLinkException {
        int numberOfLinks = 5;
        List<LinkDTO> expected = new ArrayList<>();
        for (int i = 1; i <= numberOfLinks; i++) {
            LinkDTO newLink =
                new LinkDTO(
                    URI.create("http://" + i),
                    OffsetDateTime.now(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now()
                );
            expected.add(newLink);
            jdbcLinkRepository.add(newLink);
        }

        List<LinkDTO> actual = jdbcLinkRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void testGetAllOutdatedLinks() throws AlreadyRegisteredLinkException {
        int numberOfLinks = 4;
        int numberOfOldLinks = 3;
        for (int i = 1; i <= numberOfLinks; i++) {
            OffsetDateTime date = OffsetDateTime.now();
            LinkDTO newLink = new LinkDTO(URI.create("http://" + i), date, date, date);
            jdbcLinkRepository.add(newLink);
        }
        List<LinkDTO> expected = new ArrayList<>();
        for (int i = 1; i <= numberOfOldLinks; i++) {
            OffsetDateTime date = OffsetDateTime.now().minusDays(2);
            LinkDTO newLink = new LinkDTO(URI.create("http://" + (numberOfLinks + i)), date, date, date);
            expected.add(newLink);
            jdbcLinkRepository.add(newLink);
        }

        List<LinkDTO> actual = jdbcLinkRepository.findAllOutdatedLinks(Duration.ofDays(1));

        assertEquals(expected, actual);
    }


    @Test
    @Transactional
    @Rollback
    void testRemoveLink() throws AlreadyRegisteredLinkException {
        LinkDTO firstLink =
            new LinkDTO(URI.create("http://" + 1), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        LinkDTO secondLink =
            new LinkDTO(URI.create("http://" + 2), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(firstLink);
        jdbcLinkRepository.add(secondLink);

        List<LinkDTO> links = jdbcLinkRepository.findAll();
        assertEquals(2, links.size());
        jdbcLinkRepository.remove(URI.create("http://" + 1));
        assertEquals(List.of(secondLink), jdbcLinkRepository.findAll());
    }

    @Test
    @Transactional
    @Rollback
    void testAddAlreadyExistingLink() throws AlreadyRegisteredLinkException {
        LinkDTO link =
            new LinkDTO(URI.create("http://1"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(link);

        Exception exception = assertThrows(AlreadyRegisteredLinkException.class, () ->
            jdbcLinkRepository.add(link));
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteNonExistentChat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            jdbcLinkRepository.remove(URI.create("http://sonelink")));
        assertEquals("http://sonelink cannot be deleted because it wasn`t found", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    void testUpdateLink() throws AlreadyRegisteredLinkException {
        LinkDTO link =
            new LinkDTO(URI.create("http://link"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(link);
        LinkDTO updatedLink = new LinkDTO(link.getUrl(),
            link.getCreatedTime(),
            OffsetDateTime.now().plusDays(1),
            OffsetDateTime.now().plusDays(2)
        );
        jdbcLinkRepository.update(updatedLink);

        LinkDTO expected = jdbcLinkRepository.get(updatedLink.getUrl());

        assertEquals(updatedLink, expected);
    }
}
