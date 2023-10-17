package com.hanielfialho.lobby.manager.player;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielfialho.lobby.LobbyPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlayerDateManager {

    private static final Logger logger = LoggerFactory.getLogger(PlayerDateManager.class);
    private final LobbyPlugin plugin;

    private static final long CACHE_EXPIRATION_MINUTES = 30;
    private static final long MAX_CACHE_SIZE = 1000;

    // SQL queries as constants
    private static final String SELECT_PLAYER_QUERY = "SELECT player_name FROM lobby WHERE player_name = ?";
    private static final String INSERT_PLAYER_QUERY = "INSERT INTO lobby (player_name, entry_date) VALUES (?, ?)";
    private static final String SELECT_ENTRY_DATE_QUERY = "SELECT entry_date FROM lobby WHERE player_name = ?";

    // Create a Caffeine cache
    private final Cache<String, String> entryDateCache;

    public PlayerDateManager(LobbyPlugin plugin) {
        this.plugin = plugin;

        entryDateCache = Caffeine.newBuilder()
                .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
                .maximumSize(MAX_CACHE_SIZE)
                .build();
    }

    public void setPlayerEntryDateInDatabase(String playerName) {
        plugin.getDatabaseManager().execute(connection -> {
            try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_PLAYER_QUERY)) {
                selectStatement.setString(1, playerName);
                ResultSet resultSet = selectStatement.executeQuery();

                if (!resultSet.next()) {
                    try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_PLAYER_QUERY)) {
                        String entryDate = getCurrentDateTime();
                        insertStatement.setString(1, playerName);
                        insertStatement.setString(2, entryDate);
                        insertStatement.executeUpdate();

                        // Update the cache
                        entryDateCache.put(playerName, entryDate);
                    }
                }
            } catch (SQLException e) {
                logger.error("Error while setting player entry date in database", e);
            }
        });
    }

    public String getPlayerEntryDateFromCache(String playerName) {
        // Check the cache first
        String cachedEntryDate = entryDateCache.getIfPresent(playerName);
        if (cachedEntryDate != null) {
            return cachedEntryDate;
        }

        // If not found in cache, fetch from the database and update the cache
        String entryDate = getPlayerEntryDateFromDatabase(playerName);
        if (entryDate != null) {
            entryDateCache.put(playerName, entryDate);
        }
        return entryDate;
    }

    public String getPlayerEntryDateFromDatabase(String playerName) {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(SELECT_ENTRY_DATE_QUERY)) {
            selectStatement.setString(1, playerName);
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return resultSet.getString("entry_date");
        } catch (SQLException e) {
            logger.error("Error while fetching player entry date from the database", e);
            return null;
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }
}
