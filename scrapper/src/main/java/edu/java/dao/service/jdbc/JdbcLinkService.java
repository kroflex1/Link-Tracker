package edu.java.dao.service.jdbc;

import edu.java.dao.dto.Link;
import edu.java.dao.dto.LinkAndChat;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.dao.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService implements LinkService {
    JdbcLinkRepository linkRepository;
    JdbcLinkAndChatRepository linkAndChatRepository;

    @Autowired
    public JdbcLinkService(JdbcLinkRepository linkRepository, JdbcLinkAndChatRepository linkAndChatRepository) {
        this.linkRepository = linkRepository;
        this.linkAndChatRepository = linkAndChatRepository;
    }

    @Override
    public Link add(long tgChatId, URI url) throws IllegalArgumentException {
        Link link = new Link(url, OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        linkRepository.add(link);
        linkAndChatRepository.add(new LinkAndChat(url, tgChatId));
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) throws IllegalArgumentException {
        linkAndChatRepository.remove(new LinkAndChat(url, tgChatId));
        return null;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        linkAndChatRepository.
        return linkRepository.findAll();
    }

    @Override
    public Collection<Link> listAll(long tgChatId, Duration duration) {
        return linkRepository.findAllOutdatedLinks(d);
    }

}
