package com.hanielfialho.lobby.inventory;

import com.hanielfialho.lobby.external.inventory.FastInv;
import org.bukkit.inventory.ItemStack;

public class LobbyInventory extends FastInv {
    public LobbyInventory(int size, String title) {
        super(size, title);
    }

    @Override
    public void setItemStack(int slot, ItemStack item) {
        setItem(slot, item);
    }
}
