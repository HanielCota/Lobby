package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.items.ItemAction;
import org.bukkit.entity.Player;

public class PlayerHeadAction implements ItemAction {
    @Override
    public void execute(Player player) {
        player.sendMessage("Você clicou em uma cabeça de jogador!");
    }
}