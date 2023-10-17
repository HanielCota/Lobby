package com.hanielfialho.lobby.manager.twitter;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
public class TwitterDatabaseManager {

    private LobbyPlugin plugin;

    private static final String UPDATE_TWITTER_QUERY = "UPDATE lobby SET twitter_username = ? WHERE player_name = ?";
    private static final String SELECT_TWITTER_QUERY = "SELECT twitter_username FROM lobby WHERE player_name = ?";
    private static final String DELETE_TWITTER_QUERY = "UPDATE lobby SET twitter_username = NULL WHERE player_name = ?";

    // Create a Caffeine cache
    private final Cache<String, String> twitterCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    public void setTwitterForPlayer(String playerName, String twitterUsername) {
        try {
            updateTwitterInDatabase(playerName, twitterUsername);
            // Update the cache
            twitterCache.put(playerName, twitterUsername);
        } catch (SQLException e) {
            log.error("Error while setting Twitter username for player in the database", e);
        }
    }

    public String getTwitterForPlayer(String playerName) {
        // Check the cache first
        String cachedTwitter = twitterCache.getIfPresent(playerName);
        if (cachedTwitter != null) {
            return cachedTwitter;
        }

        try {
            String twitter = queryTwitterFromDatabase(playerName);
            if (twitter != null) {
                // Put Twitter in the cache
                twitterCache.put(playerName, twitter);
            }
            return twitter;
        } catch (SQLException e) {
            log.error("Error while getting Twitter username for player from the database", e);
        }

        return null;
    }

    public void deleteTwitterForPlayer(String playerName) {
        try {
            deleteTwitterFromDatabase(playerName);
            // Remove Twitter from the cache
            twitterCache.invalidate(playerName);
        } catch (SQLException e) {
            log.error("Error while deleting Twitter username for player in the database", e);
        }
    }

    private void updateTwitterInDatabase(String... params) throws SQLException {
        try (PreparedStatement statement = plugin.getDatabaseManager()
                .getConnection()
                .prepareStatement(UPDATE_TWITTER_QUERY)) {
            statement.setString(1, params[1]);
            statement.setString(2, params[0]);

            statement.executeUpdate();
        }
    }

    private String queryTwitterFromDatabase(String... params) throws SQLException {
        try (PreparedStatement statement = plugin.getDatabaseManager()
                .getConnection()
                .prepareStatement(SELECT_TWITTER_QUERY)) {
            statement.setString(1, params[0]);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("twitter_username");
                }
            }
        }
        return null;
    }

    private void deleteTwitterFromDatabase(String... params) throws SQLException {
        try (PreparedStatement statement = plugin.getDatabaseManager()
                .getConnection()
                .prepareStatement(DELETE_TWITTER_QUERY)) {
            statement.setString(1, params[0]);

            statement.executeUpdate();
        }
    }
}
