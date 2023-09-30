package com.hanielfialho.lobby.manager.instagram;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Slf4j
public class InstagramDatabaseManager {

    private LobbyPlugin plugin;

    public CompletableFuture<Void> setInstagramForPlayerAsync(String playerName, String instagramUsername) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET instagram_username = ? WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, instagramUsername);
                statement.setString(2, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while setting Instagram username for player in the database", e);
            }
        });
    }

    public CompletableFuture<String> getInstagramForPlayerAsync(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT instagram_username FROM lobby WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("instagram_username");
                    }
                }
            } catch (SQLException e) {
                log.error("Error while getting Instagram username for player from the database", e);
            }

            return null;
        });
    }
}
