package com.example.backend.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(Environment env) {
        String url = env.getProperty("DATABASE_URL");
        if (url == null || url.isBlank()) {
            url = "jdbc:postgresql://localhost:5432/PBO1_db";
        }

        // Normalize: ensure exactly one leading "jdbc:" prefix
        if (url.startsWith("postgresql://")) {
            url = "jdbc:" + url;
        }
        // remove accidental double prefix like "jdbc:jdbc:..."
        url = url.replaceFirst("^jdbc:jdbc:", "jdbc:");
        if (!url.startsWith("jdbc:")) {
            url = "jdbc:" + url;
        }

        String username = env.getProperty("DB_USER", "postgresuser");
        String password = env.getProperty("DB_PASSWORD", "pbo123");

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName("org.postgresql.Driver");

        // Attempt to create the configured DataSource. If it fails, rethrow with
        // a clear message so startup fails fast and the user can fix `.env`/network.
        try {
            return new HikariDataSource(cfg);
        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(DataSourceConfig.class)
                    .error("Failed to initialize configured DataSource ({}). Application requires the online database to be reachable. Error: {}",
                            url, ex.getMessage());
            throw new IllegalStateException("Unable to initialize DataSource. Ensure DATABASE_URL in .env is correct and the database is reachable.", ex);
        }
    }
}
