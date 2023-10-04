package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.items.ItemAction;
import org.bukkit.entity.Player;

public class DyeAction implements ItemAction {

    @Override
    public void execute(Player player) {
        player.sendMessage("Você clicou em uma ItemSack!");
    }
}
