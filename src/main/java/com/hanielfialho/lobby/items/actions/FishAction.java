package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.items.ItemAction;
import org.bukkit.entity.Player;

public class FishAction implements ItemAction {
    @Override
    public void execute(Player player) {
        player.performCommand("cosmetic");
    }
}
