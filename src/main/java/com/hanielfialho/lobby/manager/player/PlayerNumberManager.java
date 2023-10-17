package com.hanielfialho.lobby.manager.player;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielfialho.lobby.LobbyPlugin;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PlayerNumberManager {

    private final LobbyPlugin plugin;

    private static final long CACHE_EXPIRATION_MINUTES = 30;
    private static final long MAX_CACHE_SIZE = 1000;

    // SQL queries as constants
    private static final String UPDATE_PHONE_NUMBER_QUERY = "UPDATE lobby SET phone_number = ? WHERE player_name = ?";
    private static final String SELECT_PHONE_NUMBER_QUERY = "SELECT phone_number FROM lobby WHERE player_name = ?";
    private static final String DELETE_PHONE_NUMBER_QUERY = "UPDATE lobby SET phone_number = NULL WHERE player_name = ?";

    private final Cache<String, String> phoneNumberCache = Caffeine.newBuilder()
            .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
            .maximumSize(MAX_CACHE_SIZE)
            .build();

    public PlayerNumberManager(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    public void setPhoneNumberForPlayer(String playerName, String phoneNumber) {
        try {
            updatePhoneNumberInDatabase(playerName, phoneNumber);

            // Update the cache
            phoneNumberCache.put(playerName, phoneNumber);
        } catch (SQLException e) {
            log.error("Error while setting phone number for player in the database", e);
        }
    }

    public String getPhoneNumberForPlayer(String playerName) {
        // Check the cache first
        String cachedPhoneNumber = phoneNumberCache.getIfPresent(playerName);
        if (cachedPhoneNumber != null) {
            return cachedPhoneNumber;
        }

        try {
            String phoneNumber = queryPhoneNumberFromDatabase(playerName);
            if (phoneNumber != null) {
                // Only put the phone number in the cache if it's not null
                phoneNumberCache.put(playerName, phoneNumber);
            }
            return phoneNumber;
        } catch (SQLException e) {
            log.error("Error while getting phone number for player from the database", e);
        }

        // If the phoneNumber is null, don't cache it, and return null
        return null;
    }

    public void deletePhoneForPlayer(String playerName) {
        try {
            deletePhoneNumberFromDatabase(playerName);

            // Remove the phone number from the cache
            phoneNumberCache.invalidate(playerName);
        } catch (SQLException e) {
            log.error("Error while deleting phone number for player in the database", e);
        }
    }

    private void updatePhoneNumberInDatabase(String... params) throws SQLException {
        try (PreparedStatement statement = plugin.getDatabaseManager()
                .getConnection()
                .prepareStatement(UPDATE_PHONE_NUMBER_QUERY)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        }
    }

    private String queryPhoneNumberFromDatabase(String... params) throws SQLException {
        try (PreparedStatement statement = plugin.getDatabaseManager()
                .getConnection()
                .prepareStatement(SELECT_PHONE_NUMBER_QUERY)) {
            statement.setString(1, params[0]);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("phone_number");
                }
            }
        }
        return null;
    }

    private void deletePhoneNumberFromDatabase(String... params) throws SQLException {
        try (PreparedStatement statement = plugin.getDatabaseManager()
                .getConnection()
                .prepareStatement(DELETE_PHONE_NUMBER_QUERY)) {
            statement.setString(1, params[0]);
            statement.executeUpdate();
        }
    }
}
