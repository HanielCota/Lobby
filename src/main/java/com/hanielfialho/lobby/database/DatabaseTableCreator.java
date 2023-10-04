package com.hanielfialho.lobby.database;

import com.hanielfialho.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class DatabaseTableCreator {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private final Logger logger = LoggerFactory.getLogger(DatabaseTableCreator.class);
    private final LobbyPlugin plugin;

    public void createTable(String tableName, String createTableQuery) {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                Statement statement = connection.createStatement()) {

            String createQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " " + createTableQuery;
            int result = statement.executeUpdate(createQuery);

            if (result >= 0) {
                console.sendMessage("Tabela " + tableName + " criada com sucesso.");
                return;
            }

            logger.error("Não foi possível criar a tabela {}: Unknown error", tableName);
            console.sendMessage("Não foi possível criar a tabela " + tableName + ": Unknown error");

        } catch (SQLException exception) {
            String errorMessage = "Não foi possível criar a tabela " + tableName + ": " + exception.getMessage();
            logger.error(errorMessage, exception);
            console.sendMessage(errorMessage);
        }
    }
}
