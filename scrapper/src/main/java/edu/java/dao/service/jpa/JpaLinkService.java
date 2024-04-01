package edu.java.dao.service.jpa;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jpa.JpaChatRepository;
import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.repository.jpa.entity.Chat;
import edu.java.dao.repository.jpa.entity.Link;
import edu.java.dao.service.LinkService;
import edu.java.exceptions.AlreadyTrackedLinkException;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JpaLinkService implements LinkService {
    private static final String NOT_EXIST_LINK_MESSAGE = "Can`t find link %s";
    private static final String NOT_EXIST_CHAT_MESSAGE = "Can`t find chat with id=%d";
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Autowired
    public JpaLinkService(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public LinkDTO add(long tgChatId, URI url) throws AlreadyTrackedLinkException {
        if (!linkRepository.existsById(url.toString())) {
            linkRepository.save(new Link(
                url.toString(),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
            ));
        }
        if (!chatRepository.existsById(tgChatId)) {
            chatRepository.save(new Chat(tgChatId, OffsetDateTime.now()));
        }
        Chat chat = chatRepository.findById(tgChatId).get();
        Link link = linkRepository.findById(url.toString()).get();
//        if (chat.getTrackedLinks().contains(link)) {
//            throw new AlreadyTrackedLinkException(tgChatId, url);
//        }
//        chat.addLink(link);
//        chatRepository.save(chat);
        return convertEntityToLinkDTO(link);
    }

    @Override
    public void remove(long tgChatId, URI url) throws IllegalArgumentException {
        Optional<Chat> chat = chatRepository.findById(tgChatId);
        Optional<Link> link = linkRepository.findById(url.toString());
        if (chat.isEmpty()) {
            throw new IllegalArgumentException(NOT_EXIST_CHAT_MESSAGE.formatted(tgChatId));
        } else if (link.isEmpty()) {
            throw new IllegalArgumentException(NOT_EXIST_LINK_MESSAGE.formatted(url));
        }
//        chat.get().removeLink(link.get());
        chatRepository.save(chat.get());
    }

    @Override
    public Collection<LinkDTO> listAll(long tgChatId) {
        List<LinkDTO> result = new ArrayList<>();
        linkRepository.findAll().forEach(e -> result.add(convertEntityToLinkDTO(e)));
        return result;
    }

    @Override
    public Collection<LinkDTO> getAllOutdated(Duration duration) {
        OffsetDateTime outdated = OffsetDateTime.now().minusSeconds(duration.toSeconds());
        List<LinkDTO> result = new ArrayList<>();
        linkRepository.findAllByLastCheckTimeLessThanEqual(outdated)
            .forEach(e -> result.add(convertEntityToLinkDTO(e)));
        return result;
    }

    @Override
    public void updateLastActivityTime(URI url, OffsetDateTime lastActivityTime) throws IllegalArgumentException {
        if (!linkRepository.existsById(url.toString())) {
            throw new IllegalArgumentException(NOT_EXIST_LINK_MESSAGE.formatted(url.toString()));
        }
        Link link = linkRepository.findById(url.toString()).get();
        link.setLastActivityTime(lastActivityTime);
        linkRepository.save(link);
    }

    @Override
    public void updateLastCheckTime(URI url, OffsetDateTime lastCheckTime) {
        if (!linkRepository.existsById(url.toString())) {
            throw new IllegalArgumentException(NOT_EXIST_LINK_MESSAGE.formatted(url.toString()));
        }
        Link link = linkRepository.findById(url.toString()).get();
        link.setLastCheckTime(lastCheckTime);
        linkRepository.save(link);
    }

    private LinkDTO convertEntityToLinkDTO(Link entity) {
        return new LinkDTO(
            URI.create(entity.getLink()),
            entity.getCreatedAt(),
            entity.getLastCheckTime(),
            entity.getLastActivityTime()
        );
    }
}
