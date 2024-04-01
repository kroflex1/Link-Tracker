package edu.java.scrapper.dao.repository.jdbc;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.exceptions.AlreadyRegisteredLinkException;
import edu.java.exceptions.AlreadyTrackedLinkException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
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
@Transactional
public class JdbcLinkAndChatRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkAndChatRepository linkAndChatRepository;
    @Autowired
    JdbcChatRepository chatRepository;
    @Autowired
    JdbcLinkRepository linkRepository;

    @Test
    @Rollback
    void testAddNewChat()
        throws AlreadyRegisteredChatException, AlreadyRegisteredLinkException, AlreadyTrackedLinkException {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        LinkDTO
            link = new LinkDTO(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        LinkAndChatDTO expected = new LinkAndChatDTO(link.getUrl(), chat.getChatId());
        chatRepository.add(chat);
        linkRepository.add(link);
        linkAndChatRepository.add(expected);

        List<LinkAndChatDTO> actual = linkAndChatRepository.findAll();

        assertEquals(expected, actual.getFirst());
    }

    @Test
    @Rollback
    void tesGetAllChats()
        throws AlreadyRegisteredLinkException, AlreadyRegisteredChatException, AlreadyTrackedLinkException {
        int numberOfRecords = 5;
        List<LinkAndChatDTO> expected = new ArrayList<>();
        for (long i = 1; i <= numberOfRecords; i++) {
            URI link = URI.create("http://" + i);
            linkRepository.add(new LinkDTO(link, OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now()));
            chatRepository.add(new ChatDTO(i, OffsetDateTime.now()));
            LinkAndChatDTO newRecord = new LinkAndChatDTO(link, i);
            expected.add(newRecord);
            linkAndChatRepository.add(newRecord);
        }

        List<LinkAndChatDTO> actual = linkAndChatRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void testRemoveChatByLinkAndChatId()
        throws AlreadyRegisteredChatException, AlreadyRegisteredLinkException, AlreadyTrackedLinkException {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        LinkDTO
            link = new LinkDTO(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        LinkAndChatDTO record = new LinkAndChatDTO(link.getUrl(), chat.getChatId());
        chatRepository.add(chat);
        linkRepository.add(link);
        linkAndChatRepository.add(record);

        List<LinkAndChatDTO> records = linkAndChatRepository.findAll();
        assertEquals(1, records.size());
        linkAndChatRepository.remove(record);
        assertEquals(new ArrayList<>(), linkAndChatRepository.findAll());
    }

    @Test
    @Rollback
    void testAddAlreadyExistsLinkAndChat()
        throws AlreadyRegisteredChatException, AlreadyRegisteredLinkException, AlreadyTrackedLinkException {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        LinkDTO
            link = new LinkDTO(URI.create("http://somelink"), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        LinkAndChatDTO record = new LinkAndChatDTO(link.getUrl(), chat.getChatId());
        chatRepository.add(chat);
        linkRepository.add(link);
        linkAndChatRepository.add(record);

        assertThrows(AlreadyTrackedLinkException.class, () ->
            linkAndChatRepository.add(record));
    }

    @Test
    @Rollback
    void testRemoveNonExistentRecord() {
        LinkAndChatDTO record = new LinkAndChatDTO(URI.create("http://somelink"), 1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            linkAndChatRepository.remove(record));
        assertEquals("Chat with link http://somelink wasn`t found", exception.getMessage());
    }
}
