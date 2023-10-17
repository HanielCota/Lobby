package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.inventory.ProfileInventory;
import com.hanielfialho.lobby.inventory.factories.ProfileFactory;
import com.hanielfialho.lobby.items.ItemAction;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PlayerHeadAction implements ItemAction {

    @Override
    public void execute(Player player) {

        ProfileInventory profileInventory = ProfileFactory.createProfileInventory(player);
        profileInventory.open(player);
    }
}