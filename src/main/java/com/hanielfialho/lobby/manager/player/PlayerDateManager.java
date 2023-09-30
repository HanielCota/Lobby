package com.hanielfialho.lobby.manager.player;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class PlayerDateManager {

    private final LobbyPlugin plugin;

    private static final Logger logger = LoggerFactory.getLogger(PlayerDateManager.class);

    public CompletableFuture<Void> setPlayerEntryDateInDatabaseAsync(String playerName) {
        return CompletableFuture.runAsync(() -> {
            String selectQuery = "SELECT player_name FROM lobby WHERE player_name = ?";
            String insertQuery = "INSERT INTO lobby (player_name, entry_date) VALUES (?, ?)";

            plugin.getDatabaseManager().execute(connection -> {
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setString(1, playerName);
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (!resultSet.next()) {
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            String entryDate = getCurrentDateTime();
                            insertStatement.setString(1, playerName);
                            insertStatement.setString(2, entryDate);
                            insertStatement.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    logger.error("Error while setting player entry date in database", e);
                }
            });
        });
    }


    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }
}
