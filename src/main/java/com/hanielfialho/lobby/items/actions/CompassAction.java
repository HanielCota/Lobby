package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.inventory.CompassInventory;
import com.hanielfialho.lobby.inventory.factories.CompassInventoryFactory;
import com.hanielfialho.lobby.items.ItemAction;
import org.bukkit.entity.Player;

public class CompassAction implements ItemAction {

    @Override
    public void execute(Player player) {

        CompassInventory compassInventory = CompassInventoryFactory.createCompassInventory();
        compassInventory.open(player);
    }
}