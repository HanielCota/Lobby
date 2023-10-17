package com.hanielfialho.lobby.manager.discord;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielfialho.lobby.LobbyPlugin;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DiscordDatabaseManager {

    private final Cache<String, String> discordCache = Caffeine.newBuilder().build();
    private final LobbyPlugin plugin;

    public DiscordDatabaseManager(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    public void setDiscordForPlayer(String playerName, String discordUsername) {
        try {
            updateDatabase("UPDATE lobby SET discord_username = ? WHERE player_name = ?", playerName, discordUsername);
        } catch (SQLException e) {
            log.error("Error while setting Discord username for player in the database", e);
        }
    }

    public String getDiscordForPlayer(String playerName) {
        String cachedValue = discordCache.getIfPresent(playerName);
        if (cachedValue != null) {
            return cachedValue;
        }

        try {
            String discordUsername = queryDatabase(playerName);
            if (discordUsername != null) {
                discordCache.put(playerName, discordUsername);
                log.info("Added to cache: playerName={}, discordUsername={}", playerName, discordUsername);
            }
            return discordUsername;
        } catch (SQLException e) {
            log.error("Error while getting Discord username for player from the database", e);
        }

        return null;
    }

    public void deleteDiscordForPlayer(String playerName) {
        try {
            updateDatabase("UPDATE lobby SET discord_username = NULL WHERE player_name = ?", playerName);
        } catch (SQLException e) {
            log.error("Error while deleting Discord username for player in the database", e);
        }
    }

    private void updateDatabase(String query, String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
            discordCache.invalidate(params[0]);
            log.info("Removed from cache: playerName={}", params[0]);
        }
    }

    private String queryDatabase(String... params) throws SQLException {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT discord_username FROM lobby WHERE player_name = ?")) {
            statement.setString(1, params[0]);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("discord_username");
                }
            }
        }
        return null;
    }
}
