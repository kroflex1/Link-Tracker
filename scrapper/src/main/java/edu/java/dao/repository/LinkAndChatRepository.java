package edu.java.dao.repository;

import edu.java.dao.dto.LinkAndChatDTO;
import java.net.URI;
import java.util.List;

public interface LinkAndChatRepository extends SimpleCrudRepository<LinkAndChatDTO, LinkAndChatDTO> {
    List<LinkAndChatDTO> findAll(URI link);

    List<LinkAndChatDTO> finaAll(Long chatId);
}
