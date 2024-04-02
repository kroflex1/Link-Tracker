package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    @NotEmpty
    @Value("${spring.datasource.url}")
    String url;
    @NotEmpty
    @Value("${spring.datasource.username}")
    String username;
    @NotEmpty
    @Value("${spring.datasource.password}")
    String password;
    @NotEmpty
    @Value("${spring.datasource.driver}")
    String driver;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setDriverClassName(driver);
        return driverManagerDataSource;
    }
}
