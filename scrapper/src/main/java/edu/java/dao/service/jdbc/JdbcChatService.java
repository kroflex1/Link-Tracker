package edu.java.dao.service.jdbc;

import edu.java.dao.dto.Chat;
import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Autowired
    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void register(long chatId) throws IllegalArgumentException {
        chatRepository.add(new Chat(chatId, OffsetDateTime.now()));
    }

    @Override
    public void unregister(long chatId) {
        chatRepository.remove(chatId);
    }
}
