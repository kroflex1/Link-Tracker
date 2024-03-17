package edu.java.dao.service.jdbc;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.repository.jdbc.JdbcChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.service.ChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkAndChatRepository linkAndChatRepository;

    @Autowired
    public JdbcChatService(JdbcChatRepository chatRepository, JdbcLinkAndChatRepository linkAndChatRepository) {
        this.chatRepository = chatRepository;
        this.linkAndChatRepository = linkAndChatRepository;
    }

    @Override
    public void register(long chatId) throws IllegalArgumentException {
        chatRepository.add(new ChatDTO(chatId, OffsetDateTime.now()));
    }

    @Override
    public void unregister(long chatId) {
        chatRepository.remove(chatId);
    }

    @Override
    public List<LinkAndChatDTO> getChatsThatTrackLink(URI url) {
        return linkAndChatRepository.findAll(url);
    }
}
