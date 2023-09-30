package com.hanielfialho.lobby.manager.email;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Slf4j
public class EmailDatabaseManager {

    private LobbyPlugin plugin;

    public CompletableFuture<Void> setEmailForPlayerAsync(String playerName, String email) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET player_email = ? WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while setting email for player in the database", e);
            }
        });
    }

    public CompletableFuture<String> getEmailForPlayerAsync(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT player_email FROM lobby WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("player_email");
                    }
                }
            } catch (SQLException e) {
                log.error("Error while getting email for player from the database", e);
            }

            return null;
        });
    }
}
