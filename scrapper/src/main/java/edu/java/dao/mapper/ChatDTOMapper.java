package edu.java.dao.mapper;

import edu.java.dao.dto.Chat;
import edu.java.utils.TimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatDTOMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Chat(
            rs.getLong("chat_id"),
            TimeManager.convertTimestampToOffsetDateTime(rs.getTimestamp("created_at"))
        );
    }

}
