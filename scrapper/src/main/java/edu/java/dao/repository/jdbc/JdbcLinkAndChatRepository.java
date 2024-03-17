package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.LinkAndChat;
import edu.java.dao.mapper.LinkAndChatMapper;
import edu.java.dao.repository.LinkAndChatRepository;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcLinkAndChatRepository implements LinkAndChatRepository {
    private static final LinkAndChatMapper LINK_AND_CHAT_MAPPER = new LinkAndChatMapper();
    private static final String SQL_INSERT = "INSERT INTO link_and_chat(link, chat_id) VALUES(?,?)";
    private static final String SQL_DELETE_BY_LINK_AND_CHATID =
        "DELETE FROM link_and_chat WHERE link = ? AND chat_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM link_and_chat";

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkAndChatRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(LinkAndChat record) throws IllegalArgumentException {
        try {
            jdbcTemplate.update(SQL_INSERT, record.getLink().toString(), record.getChatId());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(String.format(
                "This chat with id %d is already tracking link %s",
                record.getChatId(),
                record.getLink()
            ));
        }
    }

    @Override
    public void remove(LinkAndChat record) throws IllegalArgumentException {
        int numberOfChangedRows =
            jdbcTemplate.update(SQL_DELETE_BY_LINK_AND_CHATID, record.getLink().toString(), record.getChatId());
        if (numberOfChangedRows == 0) {
            throw new IllegalArgumentException(String.format("Chat with link %s wasn`t found", record.getLink()));
        }
    }

    @Override
    public List<LinkAndChat> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, LINK_AND_CHAT_MAPPER);
    }
}
