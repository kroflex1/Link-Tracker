package edu.java.dao.service;

import edu.java.dao.dto.LinkAndChatDTO;
import java.net.URI;
import java.util.List;

public interface ChatService {
    void register(long tgChatId) throws IllegalArgumentException;

    void unregister(long tgChatId);

    List<LinkAndChatDTO> getChatsThatTrackLink(URI url);
}
