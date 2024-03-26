package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.scrapper.IntegrationTest;
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
public class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcChatRepository jdbcChatRepository;

    @Test
    @Rollback
    void testAddNewChat() throws AlreadyRegisteredChatException {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        jdbcChatRepository.add(chat);

        List<ChatDTO> chats = jdbcChatRepository.findAll();

        assertEquals(chat, chats.getFirst());
    }

    @Test
    @Rollback
    void tesGetAllChats() throws AlreadyRegisteredChatException {
        int numberOfChats = 5;
        List<ChatDTO> expected = new ArrayList<>();
        for (long i = 1; i <= numberOfChats; i++) {
            ChatDTO newChat = new ChatDTO(i, OffsetDateTime.now());
            expected.add(newChat);
            jdbcChatRepository.add(newChat);
        }

        List<ChatDTO> actual = jdbcChatRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void testRemoveChat() throws AlreadyRegisteredChatException {
        ChatDTO firstChat = new ChatDTO(1L, OffsetDateTime.now());
        ChatDTO secondChat = new ChatDTO(2L, OffsetDateTime.now());
        jdbcChatRepository.add(firstChat);
        jdbcChatRepository.add(secondChat);

        List<ChatDTO> chats = jdbcChatRepository.findAll();
        assertEquals(2, chats.size());
        jdbcChatRepository.remove(1L);

        assertEquals(List.of(secondChat), jdbcChatRepository.findAll());
    }

    @Test
    @Rollback
    void testAddAlreadyExistingChat() throws AlreadyRegisteredChatException {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        jdbcChatRepository.add(chat);

        assertThrows(AlreadyRegisteredChatException.class, () ->
            jdbcChatRepository.add(chat));
    }

    @Test
    @Rollback
    void testDeleteNonExistentChat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            jdbcChatRepository.remove(1L));
        assertEquals("Chat with ID=1 was not detected", exception.getMessage());
    }

}
