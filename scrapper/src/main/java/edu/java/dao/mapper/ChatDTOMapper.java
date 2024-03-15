package edu.java.dao.mapper;

import edu.java.dao.dto.ChatDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import edu.java.utils.TimeManager;
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
