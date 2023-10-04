package com.hanielfialho.lobby.manager.twitter;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Slf4j
public class TwitterDatabaseManager {

    private LobbyPlugin plugin;

    public CompletableFuture<Void> setTwitterForPlayerAsync(String playerName, String twitterUsername) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET twitter_username = ? WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, twitterUsername);
                statement.setString(2, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while setting Twitter username for player in the database", e);
            }
        });
    }

    public CompletableFuture<String> getTwitterForPlayerAsync(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT twitter_username FROM lobby WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("twitter_username");
                    }
                }
            } catch (SQLException e) {
                log.error("Error while getting Twitter username for player from the database", e);
            }

            return null;
        });
    }

    public CompletableFuture<Void> deleteTwitterForPlayerAsync(String playerName) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET twitter_username = NULL WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while deleting Twitter username for player in the database", e);
            }
        });
    }
}
