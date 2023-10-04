package com.hanielfialho.lobby.manager.country;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class CountryDatabaseManager {

    private final LobbyPlugin plugin;
    private final Map<String, String> playerCountryCache = new ConcurrentHashMap<>();

    private String getPlayerCountryFromDatabase(String playerName) {
        String query = "SELECT player_country FROM lobby WHERE player_name = ?";
        try (PreparedStatement statement = plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("player_country");
                }
            }
        } catch (SQLException e) {
            log.error("Error while getting country for player from the database", e);
        }
        return null;
    }

    private String cacheOrRetrievePlayerCountry(String playerName) {
        return playerCountryCache.computeIfAbsent(playerName, this::getPlayerCountryFromDatabase);
    }

    public CompletableFuture<Void> setCountryForPlayerAsync(String playerName, String country) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET player_country = ? WHERE player_name = ?";
            try (PreparedStatement statement = plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, country);
                statement.setString(2, playerName);
                statement.executeUpdate();
                playerCountryCache.put(playerName, country);
                log.info("Added to cache: playerName={}, country={}", playerName, country);
            } catch (SQLException e) {
                log.error("Error while setting country for player in the database", e);
            }
        });
    }

    public CompletableFuture<String> getCountryForPlayerAsync(String playerName) {
        // Check the cache first
        String cachedCountry = playerCountryCache.get(playerName);
        if (cachedCountry != null) {
            log.info("Cache hit: playerName={}, country={}", playerName, cachedCountry);
            return CompletableFuture.completedFuture(cachedCountry);
        }

        // If not in cache, retrieve from database and add to cache
        return CompletableFuture.supplyAsync(() -> {
            String country = cacheOrRetrievePlayerCountry(playerName);
            if (country != null) {
                log.info("Cache miss: playerName={}, country={}", playerName, country);
            }
            return country;
        });
    }

    public CompletableFuture<Void> deleteCountryForPlayerAsync(String playerName) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET player_country = NULL WHERE player_name = ?";
            try (PreparedStatement statement = plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);
                statement.executeUpdate();
                playerCountryCache.remove(playerName);
                log.info("Removed from cache: playerName={}", playerName);
            } catch (SQLException e) {
                log.error("Error while deleting country for player in the database", e);
            }
        });
    }
}
