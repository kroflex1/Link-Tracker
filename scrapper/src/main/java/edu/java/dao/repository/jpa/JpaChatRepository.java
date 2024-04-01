package edu.java.dao.repository.jpa;

import edu.java.dao.repository.jpa.entity.Chat;
import org.springframework.data.repository.CrudRepository;

public interface JpaChatRepository extends CrudRepository<Chat, Long> {
}
