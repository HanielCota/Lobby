package com.hanielfialho.lobby.listeners.players;

import com.hanielfialho.lobby.LobbyPlugin;
import com.hanielfialho.lobby.manager.player.PlayerDateManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final LobbyPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        handlePlayerEntry(player);
    }

    private void handlePlayerEntry(Player player) {
        PlayerDateManager dateManager = new PlayerDateManager(plugin);
        dateManager.setPlayerEntryDateInDatabaseAsync(player.getName());
    }
}