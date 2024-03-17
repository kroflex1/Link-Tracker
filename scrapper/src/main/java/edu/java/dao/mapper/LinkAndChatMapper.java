package edu.java.dao.mapper;

import edu.java.dao.dto.LinkAndChat;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LinkAndChatMapper implements RowMapper<LinkAndChat> {
    @Override
    public LinkAndChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LinkAndChat(URI.create(rs.getString("link")), rs.getLong("chat_id"));
    }
}
