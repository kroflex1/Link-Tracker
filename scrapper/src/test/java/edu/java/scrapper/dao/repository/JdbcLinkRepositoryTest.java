package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
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
public class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Transactional
    @Rollback
    void testAddNewLink() {
        LinkDTO linkDTO = new LinkDTO(URI.create("http://link"), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(linkDTO);

        List<LinkDTO> links = jdbcLinkRepository.findAll();

        assertEquals(linkDTO, links.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllLinks() {
        int numberOfChats = 5;
        List<LinkDTO> expected = new ArrayList<>();
        for (int i = 1; i <= numberOfChats; i++) {
            LinkDTO newLink = new LinkDTO(URI.create("http://" + i), OffsetDateTime.now(), OffsetDateTime.now());
            expected.add(newLink);
            jdbcLinkRepository.add(newLink);
        }

        List<LinkDTO> actual = jdbcLinkRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveLink() {
        LinkDTO firstLinkDTO = new LinkDTO(URI.create("http://" + 1), OffsetDateTime.now(), OffsetDateTime.now());
        LinkDTO secondLinkDTO = new LinkDTO(URI.create("http://" + 2), OffsetDateTime.now(), OffsetDateTime.now());
        jdbcLinkRepository.add(firstLinkDTO);
        jdbcLinkRepository.add(secondLinkDTO);

        List<LinkDTO> links = jdbcLinkRepository.findAll();
        assertEquals(2, links.size());
        jdbcLinkRepository.remove(URI.create("http://" + 1));
        assertEquals(List.of(secondLinkDTO), jdbcLinkRepository.findAll());
    }
}
