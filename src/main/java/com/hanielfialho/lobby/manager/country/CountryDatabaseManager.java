package com.hanielfialho.lobby.manager.country;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielfialho.lobby.LobbyPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class CountryDatabaseManager {

    private final LobbyPlugin plugin;

    private final Cache<String, String> playerCountryCache =
            Caffeine.newBuilder().build();
    private static final String SELECT_COUNTRY_QUERY = "SELECT player_country FROM lobby WHERE player_name = ?";
    private static final String UPDATE_COUNTRY_QUERY = "UPDATE lobby SET player_country = ? WHERE player_name = ?";
    private static final String DELETE_COUNTRY_QUERY = "UPDATE lobby SET player_country = NULL WHERE player_name = ?";

    public void setCountryForPlayer(String playerName, String country) {
        executeUpdate(UPDATE_COUNTRY_QUERY, playerName, country);
        playerCountryCache.put(playerName, country);
    }

    public String getCountryForPlayer(String playerName) {
        String cachedCountry = playerCountryCache.getIfPresent(playerName);
        if (cachedCountry != null) {
            return cachedCountry;
        }

        String country = cacheOrRetrievePlayerCountry(playerName);
        if (country != null) {
            playerCountryCache.put(playerName, country); // Adicionar ao cache
        }
        return country;
    }

    public void deleteCountryForPlayer(String playerName) {
        executeUpdate(DELETE_COUNTRY_QUERY, playerName);
        playerCountryCache.invalidate(playerName);
    }

    private void executeUpdate(String query, String... params) {
        try (PreparedStatement statement =
                plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while executing database query: {}", e.getMessage(), e);
        }
    }

    private String cacheOrRetrievePlayerCountry(String playerName) {
        try (PreparedStatement statement =
                plugin.getDatabaseManager().getConnection().prepareStatement(SELECT_COUNTRY_QUERY)) {
            statement.setString(1, playerName);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("player_country");
                }
            }
        } catch (SQLException e) {
            log.error("Error while getting country for player from the database", e);
        }
        return null;
    }
}
