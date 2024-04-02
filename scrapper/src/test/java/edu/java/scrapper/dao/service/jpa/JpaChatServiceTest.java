package edu.java.scrapper.dao.service.jpa;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.repository.jpa.JpaChatRepository;
import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.service.jpa.JpaChatService;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class JpaChatServiceTest extends IntegrationTest {
    @Autowired
    JpaChatRepository jpaChatRepository;
    @Autowired
    JpaLinkRepository jpaLinkRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Rollback
    void testRegisterNewChat() {
        JpaChatService jpaChatService = new JpaChatService(jpaChatRepository, jpaLinkRepository);
        Long chatId = 1L;
        jpaChatService.register(chatId);

        List<ChatDTO> chats = jpaChatService.getAllChats();

        assertEquals(1, chats.size());
        assertEquals(chatId, chats.getFirst().getChatId());
    }

    @Test
    @Rollback
    void testRegisterAlreadyExistChat() {
        JpaChatService jpaChatService = new JpaChatService(jpaChatRepository, jpaLinkRepository);
        Long chatId = 1L;
        jpaChatService.register(chatId);

        Exception exception = assertThrows(AlreadyRegisteredChatException.class, () ->
            jpaChatService.register(chatId));
        assertEquals("Chat with %d already registered".formatted(chatId), exception.getMessage());

    }

    @Test
    @Rollback
    void testUnregisterChat() {
        JpaChatService jpaChatService = new JpaChatService(jpaChatRepository, jpaLinkRepository);
        Long chatId = 1L;
        jpaChatService.register(chatId);
        assertEquals(1, jpaChatService.getAllChats().size());

        jpaChatService.unregister(chatId);

        assertEquals(0, jpaChatService.getAllChats().size());
    }

    @Test
    @Rollback
    void testGetAllChats() {
        JpaChatService jpaChatService = new JpaChatService(jpaChatRepository, jpaLinkRepository);
        int numberOfChats = 5;
        Set<Long> expected = new HashSet<>();
        for (long chatId = 0; chatId < numberOfChats; chatId++) {
            expected.add(chatId);
            jpaChatService.register(chatId);
        }

        Set<Long> actual = jpaChatService
            .getAllChats()
            .stream()
            .map(ChatDTO::getChatId)
            .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

}
