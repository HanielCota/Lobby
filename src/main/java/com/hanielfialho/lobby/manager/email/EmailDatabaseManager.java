package com.hanielfialho.lobby.manager.email;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielfialho.lobby.LobbyPlugin;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EmailDatabaseManager {

    private LobbyPlugin plugin;

    private static final long CACHE_EXPIRATION_MINUTES = 30;
    private static final long MAX_CACHE_SIZE = 1000;

    private static final String UPDATE_EMAIL_QUERY = "UPDATE lobby SET player_email = ? WHERE player_name = ?";
    private static final String SELECT_EMAIL_QUERY = "SELECT player_email FROM lobby WHERE player_name = ?";
    private static final String DELETE_EMAIL_QUERY = "UPDATE lobby SET player_email = NULL WHERE player_name = ?";

    // Create a Caffeine cache
    private final Cache<String, String> emailCache;

    public EmailDatabaseManager(LobbyPlugin plugin) {
        this.plugin = plugin;

        emailCache = Caffeine.newBuilder()
                .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
                .maximumSize(MAX_CACHE_SIZE)
                .build();
    }

    public void setEmailForPlayer(String playerName, String email) {
        try {
            updateEmailInDatabase(playerName, email);

            // Update the cache
            emailCache.put(playerName, email);
            log.info("Added to cache: playerName={}, email={}", playerName, email);
        } catch (SQLException e) {
            log.error("Error while setting email for player in the database", e);
        }
    }

    public String getEmailForPlayer(String playerName) {
        // Check the cache first
        String cachedEmail = emailCache.getIfPresent(playerName);
        if (cachedEmail != null) {
            log.info("Cache hit: playerName={}, email={}", playerName, cachedEmail);
            return cachedEmail;
        }

        try {
            String email = queryEmailFromDatabase(playerName);
            if (email != null) {
                // Put the email in the cache
                emailCache.put(playerName, email);
                log.info("Cache miss: playerName={}, email={}", playerName, email);
                return email;
            }
        } catch (SQLException e) {
            log.error("Error while getting email for player from the database", e);
        }

        return null;
    }

    public void deleteEmailForPlayer(String playerName) {
        // Check if the email is in the cache before attempting to delete from the database
        if (emailCache.getIfPresent(playerName) != null) {
            emailCache.invalidate(playerName);
            log.info("Removed from cache: playerName={}", playerName);
        }

        try {
            deleteEmailFromDatabase(playerName);
        } catch (SQLException e) {
            log.error("Error while deleting email for player in the database", e);
        }
    }

    private void updateEmailInDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement = connection.prepareStatement(EmailDatabaseManager.UPDATE_EMAIL_QUERY)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        }
    }

    private String queryEmailFromDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement = connection.prepareStatement(EmailDatabaseManager.SELECT_EMAIL_QUERY)) {
            statement.setString(1, params[0]);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("player_email");
                }
            }
        }
        return null;
    }

    private void deleteEmailFromDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement = connection.prepareStatement(EmailDatabaseManager.DELETE_EMAIL_QUERY)) {
            statement.setString(1, params[0]);
            statement.executeUpdate();
        }
    }
}
