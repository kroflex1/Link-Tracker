package edu.java.dao.service.jpa;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.repository.ChatRepository;
import edu.java.dao.repository.jpa.JpaChatRepository;
import edu.java.dao.repository.jpa.entity.Chat;
import edu.java.exceptions.AlreadyRegisteredChatException;
import edu.java.exceptions.AlreadyRegisteredDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class JpaChatService implements ChatRepository {

    private final JpaChatRepository chatRepository;

    @Autowired
    public JpaChatService(JpaChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void add(ChatDTO chatDTO) throws AlreadyRegisteredChatException {
        if (chatRepository.existsById(chatDTO.getChatId())) {
            throw new AlreadyRegisteredChatException(chatDTO.getChatId());
        }
        chatRepository.save(convertCharDTOToEntity(chatDTO));
    }

    @Override
    public void remove(Long chatId) throws IllegalArgumentException {
        if (!chatRepository.existsById(chatId)) {
            throw new IllegalArgumentException("Chat with id=%d wasn`t found".formatted(chatId));
        }
        chatRepository.deleteById(chatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        List<ChatDTO> result = new ArrayList<>();
        chatRepository.findAll().forEach(e -> result.add(convertEntityToChatDTO(e)));
        return result;
    }

    private Chat convertCharDTOToEntity(ChatDTO chatDTO) {
        return new Chat(chatDTO.getChatId(), chatDTO.getCreatedAt());
    }

    private ChatDTO convertEntityToChatDTO(Chat entity) {
        return new ChatDTO(entity.getChatId(), entity.getCreatedAt());
    }

}
