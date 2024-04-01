package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
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
    @NotEmpty
    @Value("${spring.jpa.properties.hibernate.dialect}")
    String dialect;
    @NotEmpty
    @Value("${spring.jpa.hibernate.ddl-auto}")
    String ddlAuto;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setDriverClassName(driver);
        return driverManagerDataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("edu.java.dao.repository.jpa.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        return hibernateProperties;
    }
}
