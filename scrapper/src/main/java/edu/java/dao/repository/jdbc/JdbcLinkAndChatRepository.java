package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.mapper.LinkAndChatMapper;
import java.net.URI;
import java.util.List;
import javax.sql.DataSource;
import edu.java.dao.repository.LinkAndChatRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcLinkAndChatRepository implements LinkAndChatRepository {
    private static final LinkAndChatMapper LINK_AND_CHAT_MAPPER = new LinkAndChatMapper();
    private static final String SQL_INSERT = "INSERT INTO link_and_chat(link, chat_id) VALUES(?,?)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM link_and_chat WHERE id = ?";
    private static final String SQL_DELETE_BY_LINK_AND_CHATID =
        "DELETE FROM link_and_chat WHERE link = ? AND chat_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM link_and_chat";

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkAndChatRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(URI link, long chatId) {
        jdbcTemplate.update(SQL_INSERT, link.toString(), chatId);
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    @Override
    public void remove(URI link, long chatId) {
        jdbcTemplate.update(SQL_DELETE_BY_LINK_AND_CHATID, link.toString(), chatId);
    }

    @Override
    public List<LinkAndChatDTO> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, LINK_AND_CHAT_MAPPER);
    }
}
