package edu.java.scrapper.dao.service.jpa;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.service.jpa.JpaChatService;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class JpaChatServiceTest extends IntegrationTest {
    @Autowired JpaChatService jpaChatService;

    @Test
    @Rollback
    void testAddNewChat() {
        ChatDTO expected = new ChatDTO(1L, OffsetDateTime.now());
        jpaChatService.add(expected);

        List<ChatDTO> chats = jpaChatService.findAll();

        assertEquals(1, chats.size());
        assertEquals(expected, chats.getFirst());
    }

    @Test
    @Rollback
    void testAddAlreadyExistChat() {
        ChatDTO expected = new ChatDTO(1L, OffsetDateTime.now());
        jpaChatService.add(expected);

        Exception exception = assertThrows(AlreadyRegisteredChatException.class, () ->
            jpaChatService.add(expected));
        assertEquals("Chat with 1 already registered", exception.getMessage());

    }

    @Test
    @Rollback
    void testRemoveChat() {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        jpaChatService.add(chat);
        assertEquals(1, jpaChatService.findAll().size());

        jpaChatService.remove(chat.getChatId());

        assertEquals(0, jpaChatService.findAll().size());
    }

    @Test
    @Rollback
    void testGetAllChats() {
        int numberOfChats = 5;
        List<ChatDTO> expected = new ArrayList<>();
        for (long i = 0; i < numberOfChats; i++) {
            ChatDTO chatDTO = new ChatDTO(i, OffsetDateTime.now());
            expected.add(chatDTO);
            jpaChatService.add(chatDTO);
        }

        List<ChatDTO> actual = jpaChatService.findAll();

        assertEquals(expected, actual);
    }

}
