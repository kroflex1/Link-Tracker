package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcLinkAndChatRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkAndChatRepository linkAndChatRepository;
    @Autowired
    JdbcChatRepository chatRepository;
    @Autowired
    JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void testAddNewChat() {
        ChatDTO chatDTO = new ChatDTO(1L, OffsetDateTime.now());
        LinkDTO linkDTO = new LinkDTO(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now());
        chatRepository.add(chatDTO);
        linkRepository.add(linkDTO);
        linkAndChatRepository.add(linkDTO.getLink(), chatDTO.getId());

        LinkAndChatDTO expected = new LinkAndChatDTO(1L, linkDTO.getLink(), chatDTO.getId());
        List<LinkAndChatDTO> actual = linkAndChatRepository.findAll();


        assertEquals(expected, actual.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllChats() {
        int numberOfRecords = 5;
        List<LinkAndChatDTO> expected = new ArrayList<>();
        for (long i = 1; i <= numberOfRecords; i++) {
            URI link = URI.create("http://" + i);
            linkRepository.add(new LinkDTO(link, OffsetDateTime.now(), OffsetDateTime.now()));
            chatRepository.add(new ChatDTO(i, OffsetDateTime.now()));

            LinkAndChatDTO newRecord = new LinkAndChatDTO(i, link, i);
            expected.add(newRecord);
            linkAndChatRepository.add(link, i);
        }

        List<LinkAndChatDTO> actual = linkAndChatRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveChatByLinkAndChatId() {
        ChatDTO chatDTO = new ChatDTO(1L, OffsetDateTime.now());
        LinkDTO linkDTO = new LinkDTO(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now());
        chatRepository.add(chatDTO);
        linkRepository.add(linkDTO);
        linkAndChatRepository.add(linkDTO.getLink(), chatDTO.getId());

        List<LinkAndChatDTO> records = linkAndChatRepository.findAll();
        assertEquals(1, records.size());
        linkAndChatRepository.remove(linkDTO.getLink(), chatDTO.getId());
        assertEquals(new ArrayList<>(), linkAndChatRepository.findAll());
    }
}
