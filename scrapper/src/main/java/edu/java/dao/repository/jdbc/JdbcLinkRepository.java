package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.Link;
import edu.java.dao.mapper.LinkDTOMapper;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import edu.java.dao.repository.LinkRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcLinkRepository implements LinkRepository {

    private static final LinkDTOMapper LINK_MAPPER = new LinkDTOMapper();
    private static final String SQL_INSERT_LINK = "INSERT INTO links(link, created_at, last_time_update) VALUES(?,?,?)";
    private static final String SQL_DELETE_LINK = "DELETE FROM links WHERE link = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM links";
    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(Link link) throws IllegalArgumentException {
        Timestamp createdTime =
            Timestamp.valueOf(link.getCreatedTime().toLocalDateTime());
        Timestamp lastTimeUpdate = Timestamp.valueOf(link.getLastTimeUpdate().toLocalDateTime());
        try {
            jdbcTemplate.update(SQL_INSERT_LINK, link.getUri().toString(), createdTime, lastTimeUpdate);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(String.format("%s has already been registered", link.getUri()));
        }

    }

    @Override
    public void remove(URI link) throws IllegalArgumentException {
        if (jdbcTemplate.update(SQL_DELETE_LINK, link.toString()) == 0) {
            throw new IllegalArgumentException(String.format("%s cannot be deleted because it wasn`t found", link));
        }
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, LINK_MAPPER);
    }
}
