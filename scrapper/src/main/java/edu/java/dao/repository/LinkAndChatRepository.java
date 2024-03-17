package edu.java.dao.repository;

import edu.java.dao.dto.LinkAndChat;
import java.net.URI;
import java.util.List;

public interface LinkAndChatRepository extends SimpleCrudRepository<LinkAndChat, LinkAndChat> {
    List<LinkAndChat> findAll(URI link);
    List<LinkAndChat> finaAll(Long chatId);
}
