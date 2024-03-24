package edu.java.dao.service.jdbc;

import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jdbc.JdbcLinkAndChatRepository;
import edu.java.dao.repository.jdbc.JdbcLinkRepository;
import edu.java.dao.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.java.exceptions.AlreadyRegisteredLinkException;
import edu.java.exceptions.AlreadyTrackedLinkException;
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
    public LinkDTO add(long tgChatId, URI url) throws AlreadyTrackedLinkException {
        LinkDTO link = new LinkDTO(url, OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        try{
            linkRepository.add(link);
        } catch (AlreadyRegisteredLinkException ignored) {}
        linkAndChatRepository.add(new LinkAndChatDTO(url, tgChatId));
        return link;
    }

    @Override
    public void updateLastActivityTime(URI url, OffsetDateTime lastActivityTime) {
        LinkDTO linkDTO = linkRepository.get(url);
        linkRepository.update(new LinkDTO(url, linkDTO.getCreatedTime(), linkDTO.getLastCheckTime(),
            lastActivityTime
        ));
    }

    @Override
    public void updateLastCheckTime(URI url, OffsetDateTime lastCheckTime) {
        LinkDTO linkDTO = linkRepository.get(url);
        linkRepository.update(new LinkDTO(url, linkDTO.getCreatedTime(), lastCheckTime,
            linkDTO.getLastActivityTime()
        ));
    }

    @Override
    public void remove(long tgChatId, URI url) throws IllegalArgumentException {
        linkAndChatRepository.remove(new LinkAndChatDTO(url, tgChatId));
    }

    @Override
    public Collection<LinkDTO> listAll(long tgChatId) {
        List<LinkDTO> links = new ArrayList<>();
        for (LinkAndChatDTO linkAndChat : linkAndChatRepository.finaAll(tgChatId)) {
            links.add(linkRepository.get(linkAndChat.getUrl()));
        }
        return links;
    }

    @Override
    public Collection<LinkDTO> listAllOutdated(Duration duration) {
        return linkRepository.findAllOutdatedLinks(duration);
    }
}
