package edu.java.dao.service;

import edu.java.dao.dto.LinkDTO;
import edu.java.exceptions.AlreadyTrackedLinkException;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;

public interface LinkService {
    LinkDTO startTrackLink(long tgChatId, URI url) throws AlreadyTrackedLinkException;

    void stopTrackLink(long tgChatId, URI url) throws IllegalArgumentException;

    Collection<LinkDTO> getAllTrackedLinksByChat(long tgChatId);

    Collection<LinkDTO> getAllOutdated(Duration duration);

    void updateLastActivityTime(URI url, OffsetDateTime lastActivityTime) throws IllegalArgumentException;

    void updateLastCheckTime(URI url, OffsetDateTime lastCheckTime);
}
