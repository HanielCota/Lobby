package com.hanielfialho.lobby.inventory;

import com.hanielfialho.lobby.external.inventory.FastInv;
import org.bukkit.inventory.ItemStack;

public class CompassInventory extends FastInv {
    public CompassInventory(int size, String title) {
        super(size, title);
    }

    @Override
    public void setItemStack(int slot, ItemStack item) {
        setItem(slot, item);
    }
}
