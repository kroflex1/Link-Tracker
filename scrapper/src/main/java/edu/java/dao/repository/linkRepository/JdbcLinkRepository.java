package edu.java.dao.repository.linkRepository;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.mapper.ChatDTOMapper;
import edu.java.dao.mapper.LinkDTOMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

public class JdbcLinkRepository implements LinkRepository {

    private static final LinkDTOMapper LINK_MAPPER = new LinkDTOMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String SQL_INSERT_TGCHAT = "INSERT INTO links(link, created_at, last_time_update) VALUES(?,?,?)";
    private final String SQL_DELETE_TGCHAT = "DELETE FROM links WHERE link = ?";
    private final String SQL_GET_ALL = "SELECT * FROM links";

    public JdbcLinkRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(LinkDTO linkDTO) {
        Timestamp createdTime =
            Timestamp.valueOf(linkDTO.getCreatedTime().toLocalDateTime());
        Timestamp lastTimeUpdate = Timestamp.valueOf(linkDTO.getLastTimeUpdate().toLocalDateTime());
        jdbcTemplate.update(SQL_INSERT_TGCHAT, linkDTO.getLink().toString(), createdTime, lastTimeUpdate);
    }

    @Override
    public void remove(URI link) {
        jdbcTemplate.update(SQL_DELETE_TGCHAT, link.toString());
    }

    @Override
    public List<LinkDTO> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, LINK_MAPPER);
    }
}
