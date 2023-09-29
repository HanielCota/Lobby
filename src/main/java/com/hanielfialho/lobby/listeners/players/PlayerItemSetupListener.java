package com.hanielfialho.lobby.listeners.players;

import com.hanielfialho.lobby.items.PlayerItemSetter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerItemSetupListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerItemSetter.setInitialItems(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PlayerItemSetter.setInitialItems(event.getPlayer());
    }
}
