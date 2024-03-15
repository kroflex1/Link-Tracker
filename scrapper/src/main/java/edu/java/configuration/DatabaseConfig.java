package edu.java.configuration;

import edu.java.dao.repository.JdbcChatRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.validation.annotation.Validated;
import javax.sql.DataSource;

@Validated
@ConfigurationProperties(prefix = "spring.datasource", ignoreUnknownFields = false)
public record DatabaseConfig(@NotEmpty String url, @NotEmpty String username, @NotEmpty String password,
                             @NotEmpty String driver) {

    @Bean
    public JdbcChatRepository jdbcChatRepository() {
        return new JdbcChatRepository(dataSource());
    }

    private DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setDriverClassName(driver);
        return driverManagerDataSource;
    }
}
