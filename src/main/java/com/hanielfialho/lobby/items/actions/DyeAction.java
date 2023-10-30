package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.items.ItemAction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DyeAction implements ItemAction {

    private final Map<Player, Boolean> playerVisibility = new HashMap<>();

    @Override
    public void execute(Player player) {
        boolean isCurrentlyVisible = playerVisibility.getOrDefault(player, true);

        if (isCurrentlyVisible) {
            hideAllPlayers(player);
            player.sendMessage("§cVocê escondeu todos os jogadores!");
            playerVisibility.put(player, false);
            return;
        }

        showAllPlayers(player);
        player.sendMessage("§aVocê mostrou todos os jogadores novamente!");
        playerVisibility.put(player, true);
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