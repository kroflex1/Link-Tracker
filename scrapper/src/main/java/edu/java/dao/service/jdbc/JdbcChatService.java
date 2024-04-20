package edu.java.dao.service.jdbc;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.service.ChatService;
import edu.java.exceptions.AlreadyRegisteredChatException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkAndChatRepository linkAndChatRepository;

    public JdbcChatService(JdbcChatRepository chatRepository, JdbcLinkAndChatRepository linkAndChatRepository) {
        this.chatRepository = chatRepository;
        this.linkAndChatRepository = linkAndChatRepository;
    }

    @Override
    public void register(long chatId) throws AlreadyRegisteredChatException {
        chatRepository.add(new ChatDTO(chatId, OffsetDateTime.now()));
    }

    @Override
    public void unregister(long chatId) {
        chatRepository.remove(chatId);
    }

    @Override
    public List<ChatDTO> getAllChats() {
        return chatRepository.findAll();
    }

    @Override
    public List<LinkAndChatDTO> getChatsThatTrackLink(URI url) {
        return linkAndChatRepository.findAll(url);
    }
}
