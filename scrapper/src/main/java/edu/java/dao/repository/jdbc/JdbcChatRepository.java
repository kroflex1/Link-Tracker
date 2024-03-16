package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.Chat;
import edu.java.dao.mapper.ChatDTOMapper;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import edu.java.dao.repository.ChatRepository;
import org.springframework.dao.DataAccessException;
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
    public void add(Chat chat) throws IllegalArgumentException {
        Timestamp timestamp =
            Timestamp.valueOf(chat.getCreatedAt().toLocalDateTime());
        try {
            jdbcTemplate.update(SQL_INSERT_CHAT, chat.getChatId(), timestamp);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Chat with this id has already been registered");
        }
    }

    @Override
    public void remove(Long chatId) throws IllegalArgumentException {
        if( jdbcTemplate.update(SQL_DELETE_CHAT, chatId) == 0){
            throw new IllegalArgumentException("Chat with this ID was not detected");
        }
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, CHAT_MAPPER);
    }
}
