package edu.java.scrapper.dao.repository.jpa;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.repository.jpa.experiment.JpaChatRepository;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class JpaChatRepositoryTest extends IntegrationTest {
    @Autowired
    JpaChatRepository jpaChatRepository;

    @Test
    @Rollback
    void testAddNewChat() throws AlreadyRegisteredChatException {
        ChatDTO chat = new ChatDTO(1L, OffsetDateTime.now());
        jpaChatRepository.add(chat);

        List<ChatDTO> chats = jpaChatRepository.findAll();

        assertEquals(chat, chats.getFirst());
    }
}
