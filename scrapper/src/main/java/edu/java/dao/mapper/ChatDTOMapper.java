package edu.java.dao.mapper;

import edu.java.dao.dto.ChatDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class ChatDTOMapper implements RowMapper<ChatDTO> {
    @Override
    public ChatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChatDTO(
            rs.getLong("chat_id"),
            convertTimestampToOffsetDateTime(rs.getTimestamp("created_at"))
        );
    }

    private OffsetDateTime convertTimestampToOffsetDateTime(Timestamp timestamp) {
        return timestamp.toInstant().atOffset(ZoneOffset.ofHours(5));
    }
}
