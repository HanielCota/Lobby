package com.hanielfialho.lobby.listeners.players;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlayerChatListener implements Listener {
    private final Map<Player, Instant> chatCooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (hasCooldown(player)) {
            long remainingTime = getRemainingCooldownTime(player);
            player.chat("§cVocê está em cooldown no chat. Por favor, aguarde por " + remainingTime + " segundos.");
            event.setCancelled(true);
            return;
        }

        String formattedMessage = formatMessage(player);
        chatCooldowns.put(player, Instant.now());
        event.setFormat(formattedMessage + event.getMessage());
    }

    private String formatMessage(Player player) {
        String formattedMessage = PlaceholderAPI.setPlaceholders(player, "§c§lLOBBY ▶ §r%luckperms_prefix% " + player.getName() + ": ");
        return ChatColor.translateAlternateColorCodes('&', formattedMessage);
    }

    private boolean hasCooldown(Player player) {
        Instant lastTime = chatCooldowns.get(player);
        return lastTime != null && getElapsedTime(lastTime) < 5000L;
    }

    private long getRemainingCooldownTime(Player player) {
        long elapsedTime = getElapsedTime(chatCooldowns.get(player));
        return Math.max(0L, (5000L - elapsedTime) / 1000L);
    }

    private long getElapsedTime(Instant lastTime) {
        return Instant.now().toEpochMilli() - lastTime.toEpochMilli();
    }
}