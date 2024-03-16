package edu.java.dao.mapper;

import edu.java.dao.dto.LinkAndChatDTO;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LinkAndChatMapper implements RowMapper<LinkAndChatDTO> {
    @Override
    public LinkAndChatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LinkAndChatDTO(rs.getLong("id"), URI.create(rs.getString("link")), rs.getLong("chat_id"));
    }
}
