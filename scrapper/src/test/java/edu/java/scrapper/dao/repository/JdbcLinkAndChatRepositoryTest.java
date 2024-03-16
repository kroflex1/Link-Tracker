package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.Chat;
import edu.java.dao.dto.LinkAndChat;
import edu.java.dao.dto.Link;
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
        Chat chat = new Chat(1L, OffsetDateTime.now());
        Link link = new Link(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now());
        chatRepository.add(chat);
        linkRepository.add(link);
        linkAndChatRepository.add(link.getUri(), chat.getChatId());

        LinkAndChat expected = new LinkAndChat(1L, link.getUri(), chat.getChatId());
        List<LinkAndChat> actual = linkAndChatRepository.findAll();


        assertEquals(expected, actual.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllChats() {
        int numberOfRecords = 5;
        List<LinkAndChat> expected = new ArrayList<>();
        for (long i = 1; i <= numberOfRecords; i++) {
            URI link = URI.create("http://" + i);
            linkRepository.add(new Link(link, OffsetDateTime.now(), OffsetDateTime.now()));
            chatRepository.add(new Chat(i, OffsetDateTime.now()));

            LinkAndChat newRecord = new LinkAndChat(i, link, i);
            expected.add(newRecord);
            linkAndChatRepository.add(link, i);
        }

        List<LinkAndChat> actual = linkAndChatRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveChatByLinkAndChatId() {
        Chat chat = new Chat(1L, OffsetDateTime.now());
        Link link = new Link(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now());
        chatRepository.add(chat);
        linkRepository.add(link);
        linkAndChatRepository.add(link.getUri(), chat.getChatId());

        List<LinkAndChat> records = linkAndChatRepository.findAll();
        assertEquals(1, records.size());
        linkAndChatRepository.remove(link.getUri(), chat.getChatId());
        assertEquals(new ArrayList<>(), linkAndChatRepository.findAll());
    }
}
