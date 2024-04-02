package edu.java.scrapper.dao.service.jpa;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class JpaChatServiceTest extends IntegrationTest {
    @Autowired
    ChatService jpaChatService;
    @Autowired
    LinkService jpaLinkService;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Rollback
    void testRegisterNewChat() {
        Long chatId = 1L;
        jpaChatService.register(chatId);

        List<ChatDTO> chats = jpaChatService.getAllChats();

        assertEquals(1, chats.size());
        assertEquals(chatId, chats.getFirst().getChatId());
    }

    @Test
    @Rollback
    void testRegisterAlreadyExistChat() {
        Long chatId = 1L;
        jpaChatService.register(chatId);

        Exception exception = assertThrows(AlreadyRegisteredChatException.class, () ->
            jpaChatService.register(chatId));
        assertEquals("Chat with %d already registered".formatted(chatId), exception.getMessage());

    }

    @Test
    @Rollback
    void testUnregisterChat() {
        Long chatId = 1L;
        jpaChatService.register(chatId);
        assertEquals(1, jpaChatService.getAllChats().size());

        jpaChatService.unregister(chatId);

        assertEquals(0, jpaChatService.getAllChats().size());
    }

    @Test
    @Rollback
    void testGetAllChats() {
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

    @Test
    @Rollback
    void testGetLinksThatTrackChat() {
        Long chatId = 1L;
        URI link = URI.create("http://link");
        jpaChatService.register(chatId);
        jpaLinkService.startTrackLink(chatId, link);

        URI actual = jpaChatService.getChatsThatTrackLink(link).getFirst().getUrl();

        assertEquals(link, actual);
    }

}
