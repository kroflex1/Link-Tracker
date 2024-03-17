package edu.java.dao.service;

import edu.java.dao.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;

public interface LinkService {
    Link add(long tgChatId, URI url);
    Link remove(long tgChatId, URI url);
    Collection<Link> listAll(long tgChatId);
    Collection<Link> listAllOutdated(Duration duration);
}
