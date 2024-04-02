package edu.java.dao.service.jpa;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.repository.jpa.JpaChatRepository;
import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.repository.jpa.entity.Chat;
import edu.java.dao.repository.jpa.entity.Link;
import edu.java.dao.service.ChatService;
import edu.java.exceptions.AlreadyRegisteredChatException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class JpaChatService implements ChatService {

    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    public JpaChatService(JpaChatRepository chatRepository, JpaLinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    @Transactional
    public void register(long tgChatId) throws AlreadyRegisteredChatException {
        if (chatRepository.existsById(tgChatId)) {
            throw new AlreadyRegisteredChatException(tgChatId);
        }
        Chat chat = new Chat(tgChatId, OffsetDateTime.now());
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        chatRepository.deleteById(tgChatId);
    }

    @Override
    @Transactional
    public List<ChatDTO> getAllChats() {
        List<ChatDTO> result = new ArrayList<>();
        chatRepository.findAll().forEach(e -> result.add(new ChatDTO(e.getChatId(), e.getCreatedAt())));
        return result;
    }

    @Override
    @Transactional
    public List<LinkAndChatDTO> getChatsThatTrackLink(URI url) {
        Optional<Link> link = linkRepository.findById(url.toString());
        if (link.isEmpty()) {
            return new ArrayList<>();
        }
        return link.get()
            .getChats()
            .stream()
            .map(e -> new LinkAndChatDTO(url, e.getChatId()))
            .toList();
    }
}
