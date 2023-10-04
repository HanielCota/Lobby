package com.hanielfialho.lobby.manager.player;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Slf4j
public class PlayerNumberManager {

    private LobbyPlugin plugin;

    public CompletableFuture<Void> setPhoneNumberForPlayerAsync(String playerName, String phoneNumber) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET phone_number = ? WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, phoneNumber);
                statement.setString(2, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while setting phone number for player in the database", e);
            }
        });
    }

    public CompletableFuture<String> getPhoneNumberForPlayerAsync(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT phone_number FROM lobby WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("phone_number");
                    }
                }
            } catch (SQLException e) {
                log.error("Error while getting phone number for player from the database", e);
            }

            return null;
        });
    }

    public CompletableFuture<Void> deletePhoneForPlayerAsync(String playerName) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE lobby SET phone_number = NULL WHERE player_name = ?";

            try (PreparedStatement statement =
                    plugin.getDatabaseManager().getConnection().prepareStatement(query)) {
                statement.setString(1, playerName);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Error while deleting phone for player in the database", e);
            }
        });
    }
}
