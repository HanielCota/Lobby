package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.items.ItemAction;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DyeAction implements ItemAction {

    private static final long DELAY = 1000; // 1 segundos em milissegundos
    private final Map<Player, Boolean> playerVisibility = new HashMap<>();
    private final Map<Player, Long> lastActionTime = new HashMap<>();

    @Override
    public void execute(Player player) {
        long currentTime = System.currentTimeMillis();
        long lastAction = lastActionTime.getOrDefault(player, 0L);

        if (currentTime - lastAction < DELAY) {
            player.sendMessage("§cAção em andamento. Por favor, aguarde.");
            return;
        }

        boolean isCurrentlyVisible = playerVisibility.getOrDefault(player, true);
        playerVisibility.put(player, !isCurrentlyVisible);

        if (isCurrentlyVisible) {
            hideAllPlayers(player);
            updateItem(player, "§cVocê escondeu todos os jogadores!", Material.RED_DYE, "§cEsconder jogadores", "§7Clique aqui para esconder jogadores.");
        } else {
            showAllPlayers(player);
            updateItem(player, "§aVocê mostrou todos os jogadores novamente!", Material.GREEN_DYE, "§AMostrar jogadores", "§7Clique aqui para mostrar jogadores.");
        }

        lastActionTime.put(player, currentTime);
    }

    private void updateItem(Player player, String message, Material material, String itemName, String itemLore) {
        player.sendMessage(message);

        ItemStack lobbyItem = new ItemBuilder(material)
                .setName(itemName)
                .setLore(itemLore)
                .build();
        player.getInventory().setItem(7, lobbyItem);
    }

    private void hideAllPlayers(Player player) {
        for (Player onlinePlayer : player.getWorld().getPlayers()) {
            if (onlinePlayer != player) {
                player.hidePlayer(onlinePlayer);
            }
        }
    }

    private void showAllPlayers(Player player) {
        for (Player onlinePlayer : player.getWorld().getPlayers()) {
            if (onlinePlayer != player) {
                player.showPlayer(onlinePlayer);
            }
        }
    }
}
