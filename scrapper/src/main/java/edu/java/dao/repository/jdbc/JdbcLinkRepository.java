package edu.java.dao.repository.jdbc;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.mapper.LinkDTOMapper;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import edu.java.dao.repository.LinkRepository;
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
    public void add(LinkDTO linkDTO) {
        Timestamp createdTime =
            Timestamp.valueOf(linkDTO.getCreatedTime().toLocalDateTime());
        Timestamp lastTimeUpdate = Timestamp.valueOf(linkDTO.getLastTimeUpdate().toLocalDateTime());
        jdbcTemplate.update(SQL_INSERT_LINK, linkDTO.getLink().toString(), createdTime, lastTimeUpdate);
    }

    @Override
    public void remove(URI link) {
        jdbcTemplate.update(SQL_DELETE_LINK, link.toString());
    }

    @Override
    public List<LinkDTO> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, LINK_MAPPER);
    }
}
