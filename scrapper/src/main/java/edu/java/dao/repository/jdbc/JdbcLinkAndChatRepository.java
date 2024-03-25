package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.mapper.LinkAndChatMapper;
import edu.java.dao.repository.LinkAndChatRepository;
import edu.java.exceptions.AlreadyTrackedLinkException;
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
    public void add(LinkAndChatDTO linkAndChat) throws AlreadyTrackedLinkException {
        try {
            jdbcTemplate.update(SQL_INSERT, linkAndChat.getUrl().toString(), linkAndChat.getChatId());
        } catch (DataAccessException e) {
            throw new AlreadyTrackedLinkException(linkAndChat.getChatId(), linkAndChat.getUrl());
        }
    }

    @Override
    public void remove(LinkAndChatDTO linkAndChat) throws IllegalArgumentException {
        int numberOfChangedRows =
            jdbcTemplate.update(
                SQL_DELETE_BY_LINK_AND_CHATID,
                linkAndChat.getUrl().toString(),
                linkAndChat.getChatId()
            );
        if (numberOfChangedRows == 0) {
            throw new IllegalArgumentException(String.format("Chat with link %s wasn`t found", linkAndChat.getUrl()));
        }
    }

    @Override
    public List<LinkAndChatDTO> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL_RECORDS, LINK_AND_CHAT_MAPPER);
    }

    @Override
    public List<LinkAndChatDTO> findAll(URI link) {
        return jdbcTemplate.query(SQL_GET_ALL_RECORDS_WITH_LINK, LINK_AND_CHAT_MAPPER, link.toString());
    }

    @Override
    public List<LinkAndChatDTO> finaAll(Long chatId) {
        return jdbcTemplate.query(SQL_GET_ALL_RECORDS_WITH_CHAT_ID, LINK_AND_CHAT_MAPPER, chatId);
    }
}
