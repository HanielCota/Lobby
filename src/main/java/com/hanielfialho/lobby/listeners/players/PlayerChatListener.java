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
            player.sendMessage("§cVocê está em cooldown no chat. Por favor, aguarde por " + remainingTime + " segundos.");
            event.setCancelled(true);
            return;
        }

        // Obtem a mensagem formatada para o cabeçalho do chat.
        String formattedMessage = formatMessage(player);
        chatCooldowns.put(player, Instant.now());

        // Garante que o formato da mensagem de chat é seguro e não causará uma exceção.
        String safeFormattedMessage = formattedMessage.replace("%", "%%");

        // Define o formato da mensagem de chat, garantindo compatibilidade com placeholders e o formato esperado pelo setFormat.
        event.setFormat(safeFormattedMessage + "%2$s");
    }

    private String formatMessage(Player player) {
        // Usa o PlaceholderAPI para substituir placeholders na string de formato.
        String formattedMessage = PlaceholderAPI.setPlaceholders(player, "§c§lBEDWARS ▶ §r%luckperms_prefix% " + player.getName() + ": ");

        // Traduz códigos de cor alternativos usando ChatColor.
        return ChatColor.translateAlternateColorCodes('&', formattedMessage);
    }

    private boolean hasCooldown(Player player) {
        Instant lastTime = chatCooldowns.get(player);
        return lastTime != null && getElapsedTime(lastTime) < 5000L; // Cooldown de 5 segundos
    }

    private long getRemainingCooldownTime(Player player) {
        long elapsedTime = getElapsedTime(chatCooldowns.get(player));
        return Math.max(0L, (5000L - elapsedTime) / 1000L); // Converte milissegundos em segundos
    }

    private long getElapsedTime(Instant lastTime) {
        return Instant.now().toEpochMilli() - lastTime.toEpochMilli();
    }
}
