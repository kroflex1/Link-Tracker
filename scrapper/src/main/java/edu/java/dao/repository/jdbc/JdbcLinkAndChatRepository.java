package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.LinkAndChat;
import edu.java.dao.mapper.LinkAndChatMapper;
import edu.java.dao.repository.LinkAndChatRepository;
import java.net.URI;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcLinkAndChatRepository implements LinkAndChatRepository {
    private static final LinkAndChatMapper LINK_AND_CHAT_MAPPER = new LinkAndChatMapper();
    private static final String SQL_INSERT = "INSERT INTO link_and_chat(link, chat_id) VALUES(?,?)";
    private static final String SQL_DELETE_BY_LINK_AND_CHATID =
        "DELETE FROM link_and_chat WHERE link = ? AND chat_id = ?";
    private static final String SQL_GET_ALL_RECORDS = "SELECT * FROM link_and_chat";
    private static final String SQL_GET_ALL_RECORDS_WITH_CHAT_ID = "SELECT * FROM link_and_chat WHERE chat_id = ?";
    private static final String SQL_GET_ALL_RECORDS_WITH_LINK = "SELECT * FROM link_and_chat WHERE link = ?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkAndChatRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(LinkAndChat record) throws IllegalArgumentException {
        try {
            jdbcTemplate.update(SQL_INSERT, record.getUrl().toString(), record.getChatId());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(String.format(
                "This chat with id %d is already tracking link %s",
                record.getChatId(),
                record.getUrl()
            ));
        }
    }

    @Override
    public void remove(LinkAndChat record) throws IllegalArgumentException {
        int numberOfChangedRows =
            jdbcTemplate.update(SQL_DELETE_BY_LINK_AND_CHATID, record.getUrl().toString(), record.getChatId());
        if (numberOfChangedRows == 0) {
            throw new IllegalArgumentException(String.format("Chat with link %s wasn`t found", record.getUrl()));
        }
    }

    @Override
    public List<LinkAndChat> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL_RECORDS, LINK_AND_CHAT_MAPPER);
    }

    @Override
    public List<LinkAndChat> findAll(URI link) {
        return jdbcTemplate.query(SQL_GET_ALL_RECORDS_WITH_LINK, LINK_AND_CHAT_MAPPER, link.toString());
    }

    @Override
    public List<LinkAndChat> finaAll(Long chatId) {
        return jdbcTemplate.query(SQL_GET_ALL_RECORDS_WITH_CHAT_ID, LINK_AND_CHAT_MAPPER, chatId);
    }
}
