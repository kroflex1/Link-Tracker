package edu.java.dao.mapper;

import edu.java.dao.dto.ChatDTO;
import edu.java.utils.TimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatDTOMapper implements RowMapper<ChatDTO> {
    @Override
    public ChatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChatDTO(
            rs.getLong("chat_id"),
            TimeManager.convertTimestampToOffsetDateTime(rs.getTimestamp("created_at"))
        );
    }

}
