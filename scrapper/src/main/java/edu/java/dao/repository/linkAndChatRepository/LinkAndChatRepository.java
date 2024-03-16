package edu.java.dao.repository.linkAndChatRepository;

import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.repository.SimpleCrudRepository;
import java.net.URI;
import java.util.List;

public interface LinkAndChatRepository {
    void add(URI link, long chatId);

    void remove(long id);

    void remove(URI link, long chatId);

    List<LinkAndChatDTO> findAll();
}
