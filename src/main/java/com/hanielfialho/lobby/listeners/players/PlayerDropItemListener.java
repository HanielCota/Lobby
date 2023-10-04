package com.hanielfialho.lobby.listeners.players;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.concurrent.TimeUnit;

public class PlayerDropItemListener implements Listener {

    private final Cache<String, Integer> dropCounter =
            Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!player.isOp()) {
            event.setCancelled(true);
            return;
        }

        int count = dropCounter.get(playerName, k -> 0);
        count++;
        dropCounter.put(playerName, count);

        if (count < 3) {
            player.sendMessage("§cVocê precisa soltar o item duas vezes para descartá-lo.");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10F, 10F);
            event.setCancelled(true);
        }
    }
}
