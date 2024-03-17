package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.mapper.LinkDTOMapper;
import edu.java.dao.repository.LinkRepository;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcLinkRepository implements LinkRepository {
    private static final LinkDTOMapper LINK_MAPPER = new LinkDTOMapper();
    private static final String SQL_INSERT_LINK =
        "INSERT INTO links(link, created_at, last_check_time, last_activity_time) VALUES(?,?,?,?)";
    private static final String SQL_DELETE_LINK = "DELETE FROM links WHERE link = ?";
    private static final String SQL_GET_ALL_LINKS = "SELECT * FROM links";
    private static final String SQL_GET_ALL_OUTDATED_LINKS = "SELECT * FROM links WHERE last_check_time <= ?";
    private static final String SQL_GET_BY_URL = "SELECT * FROM links WHERE link = ?";
    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(LinkDTO link) throws IllegalArgumentException {
        Timestamp createdTime =
            Timestamp.valueOf(link.getCreatedTime().toLocalDateTime());
        Timestamp lastTimeUpdate = Timestamp.valueOf(link.getLastCheckTime().toLocalDateTime());
        Timestamp lastActivityTime = Timestamp.valueOf(link.getLastActivityTime().toLocalDateTime());
        try {
            jdbcTemplate.update(
                SQL_INSERT_LINK,
                link.getUrl().toString(),
                createdTime,
                lastTimeUpdate,
                lastActivityTime
            );
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(String.format("%s has already been registered", link.getUrl()));
        }
    }

    @Override
    public void remove(URI link) throws IllegalArgumentException {
        if (jdbcTemplate.update(SQL_DELETE_LINK, link.toString()) == 0) {
            throw new IllegalArgumentException(String.format("%s cannot be deleted because it wasn`t found", link));
        }
    }

    @Override
    public LinkDTO get(URI url) throws IllegalArgumentException {
        List<LinkDTO> result = jdbcTemplate.query(SQL_DELETE_LINK, LINK_MAPPER, url.toString());
        if (result.size() != 1) {
            throw new IllegalArgumentException(String.format("Link %s was not found", url));
        }
        return result.getFirst();
    }

    @Override
    public List<LinkDTO> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL_LINKS, LINK_MAPPER);
    }

    @Override
    public List<LinkDTO> findAllOutdatedLinks(Duration duration) {
        OffsetDateTime outdated = OffsetDateTime.now().minusSeconds(duration.toSeconds());
        return jdbcTemplate.query(
            SQL_GET_ALL_OUTDATED_LINKS,
            LINK_MAPPER,
            Timestamp.valueOf(outdated.toLocalDateTime())
        );
    }
}
