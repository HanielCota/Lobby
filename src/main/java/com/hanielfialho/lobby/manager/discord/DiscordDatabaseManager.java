package com.hanielfialho.lobby.manager.discord;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Slf4j
public class DiscordDatabaseManager {

    private LobbyPlugin plugin;

    public CompletableFuture<Void> setDiscordForPlayerAsync(String playerName, String discordUsername) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET discord_username = ? WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, discordUsername);
                statement.setString(2, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while setting Discord username for player in the database", e);
            }
        });
    }

    public CompletableFuture<String> getDiscordForPlayerAsync(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT discord_username FROM lobby WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("discord_username");
                    }
                }
            } catch (SQLException e) {
                log.error("Error while getting Discord username for player from the database", e);
            }

            return null;
        });
    }

    public CompletableFuture<Void> deleteDiscordForPlayerAsync(String playerName) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET discord_username = NULL WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while deleting Discord username for player in the database", e);
            }
        });
    }
}
