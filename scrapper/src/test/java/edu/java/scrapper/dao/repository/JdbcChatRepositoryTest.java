package edu.java.scrapper.dao.repository;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.repository.chatRepository.JdbcChatRepository;
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

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    void testAddNewChat() {
        ChatDTO chatDTO = new ChatDTO(1L, OffsetDateTime.now());
        jdbcChatRepository.add(chatDTO);

        List<ChatDTO> chats = jdbcChatRepository.findAll();

        assertEquals(chatDTO, chats.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void tesGetAllChats() {
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
    @Transactional
    @Rollback
    void testRemoveChat(){
        ChatDTO firstChatDTO = new ChatDTO(1L, OffsetDateTime.now());
        ChatDTO secondChatDTO = new ChatDTO(2L, OffsetDateTime.now());
        jdbcChatRepository.add(firstChatDTO);
        jdbcChatRepository.add(secondChatDTO);

        List<ChatDTO> chats = jdbcChatRepository.findAll();
        assertEquals(2, chats.size());
        jdbcChatRepository.remove(1L);
        assertEquals(List.of(secondChatDTO), jdbcChatRepository.findAll());
    }

}
