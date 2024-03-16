package edu.java.dao.repository.chatRepository;

import edu.java.dao.dto.ChatDTO;
import edu.java.dao.mapper.ChatDTOMapper;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcChatRepository implements ChatRepository {
    private static final ChatDTOMapper CHAT_MAPPER = new ChatDTOMapper();
    private static final String SQL_INSERT_CHAT = "INSERT INTO chats(chat_id, created_at) VALUES(?,?)";
    private static final String SQL_DELETE_CHAT = "DELETE FROM chats WHERE chat_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM chats";

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(ChatDTO chatDTO) {
        Timestamp timestamp =
            Timestamp.valueOf(chatDTO.getCreatedAt().toLocalDateTime());
        jdbcTemplate.update(SQL_INSERT_CHAT, chatDTO.getId(), timestamp);
    }

    @Override
    public void remove(Long chatId) {
        jdbcTemplate.update(SQL_DELETE_CHAT, chatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, CHAT_MAPPER);
    }
}
