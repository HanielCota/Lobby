package com.hanielfialho.lobby.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(String jdbcUrl, String username, String password) {
        validateArguments(jdbcUrl, username, password);

        HikariConfig config = createHikariConfig(jdbcUrl, username, password);
        this.dataSource = new HikariDataSource(config);
    }

    private void validateArguments(String jdbcUrl, String username, String password) {
        if (jdbcUrl == null || username == null || password == null) {
            throw new IllegalArgumentException("jdbcUrl, username, and password cannot be null.");
        }
    }

    private HikariConfig createHikariConfig(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(2000);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "2048");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void execute(DatabaseOperations operation) {
        try (Connection connection = getConnection()) {
            operation.execute(connection);
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
            logger.error("Error while executing database operation: {}", e.getMessage(), e);
        }
    }
}
