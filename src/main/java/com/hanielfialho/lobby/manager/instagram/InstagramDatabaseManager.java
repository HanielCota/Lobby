package com.hanielfialho.lobby.manager.instagram;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielfialho.lobby.LobbyPlugin;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class InstagramDatabaseManager {

    private final LobbyPlugin plugin;

    private static final long CACHE_EXPIRATION_MINUTES = 30;
    private static final long MAX_CACHE_SIZE = 1000;
    private static final String UPDATE_INSTAGRAM_QUERY =
            "UPDATE lobby SET instagram_username = ? WHERE player_name = ?";
    private static final String SELECT_INSTAGRAM_QUERY = "SELECT instagram_username FROM lobby WHERE player_name = ?";
    private static final String DELETE_INSTAGRAM_QUERY =
            "UPDATE lobby SET instagram_username = NULL WHERE player_name = ?";

    // Create a Caffeine cache
    private final Cache<String, String> instagramCache = Caffeine.newBuilder()
            .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
            .maximumSize(MAX_CACHE_SIZE)
            .build();

    public InstagramDatabaseManager(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    public void setInstagramForPlayer(String playerName, String instagramUsername) {
        try {
            updateInstagramInDatabase(playerName, instagramUsername);

            // Update the cache
            instagramCache.put(playerName, instagramUsername);
            log.info("Added to cache: playerName={}, Instagram={}", playerName, instagramUsername);
        } catch (SQLException e) {
            log.error("Error while setting Instagram username for player in the database", e);
        }
    }

    public String getInstagramForPlayer(String playerName) {
        // Check the cache first
        String cachedInstagram = instagramCache.getIfPresent(playerName);
        if (cachedInstagram != null) {
            log.info("Cache hit: playerName={}, Instagram={}", playerName, cachedInstagram);
            return cachedInstagram;
        }

        try {
            String instagram = queryInstagramFromDatabase(playerName);
            if (instagram != null) {
                instagramCache.put(playerName, instagram);
                log.info("Cache miss: playerName={}, Instagram={}", playerName, instagram);
                return instagram;
            }
        } catch (SQLException e) {
            log.error("Error while getting Instagram username for player from the database", e);
        }

        return null;
    }

    public void deleteInstagramForPlayer(String playerName) {
        try {
            deleteInstagramFromDatabase(playerName);

            // Remove Instagram from the cache
            instagramCache.invalidate(playerName);
            log.info("Removed from cache: playerName={}", playerName);
        } catch (SQLException e) {
            log.error("Error while deleting Instagram username for player in the database", e);
        }
    }

    private void updateInstagramInDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_INSTAGRAM_QUERY)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        }
    }

    private String queryInstagramFromDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_INSTAGRAM_QUERY)) {
            statement.setString(1, params[0]);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("instagram_username");
                }
            }
        }
        return null;
    }

    private void deleteInstagramFromDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_INSTAGRAM_QUERY)) {
            statement.setString(1, params[0]);
            statement.executeUpdate();
        }
    }
}
