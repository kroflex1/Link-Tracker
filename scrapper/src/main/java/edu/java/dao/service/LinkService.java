package edu.java.dao.service;

import edu.java.dao.dto.LinkDTO;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;

public interface LinkService {
    LinkDTO add(long tgChatId, URI url);

    void remove(long tgChatId, URI url);

    Collection<LinkDTO> listAll(long tgChatId);

    Collection<LinkDTO> listAllOutdated(Duration duration);

    void updateLastActivityTime(URI url, OffsetDateTime lastActivityTime);

    void updateLastCheckTime(URI url, OffsetDateTime lastCheckTime);
}
