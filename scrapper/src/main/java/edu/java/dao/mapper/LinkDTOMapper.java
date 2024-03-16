package edu.java.dao.mapper;

import edu.java.dao.dto.Link;
import edu.java.utils.TimeManager;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class LinkDTOMapper implements RowMapper<Link> {
    @SneakyThrows @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link(new URI(rs.getString("link")),
            TimeManager.convertTimestampToOffsetDateTime(rs.getTimestamp("created_at")),
            TimeManager.convertTimestampToOffsetDateTime(rs.getTimestamp("last_time_update"))
        );
    }
}
