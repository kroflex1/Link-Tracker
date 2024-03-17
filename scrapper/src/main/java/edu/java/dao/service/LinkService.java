package edu.java.dao.service;

import edu.java.dao.dto.LinkDTO;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;

public interface LinkService {
    LinkDTO add(long tgChatId, URI url);

    LinkDTO remove(long tgChatId, URI url);

    Collection<LinkDTO> listAll(long tgChatId);

    Collection<LinkDTO> listAllOutdated(Duration duration);
}
