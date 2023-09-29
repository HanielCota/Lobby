package com.hanielfialho.lobby.items;

import org.bukkit.inventory.ItemStack;

public interface ItemFactory {

    ItemStack createItem(int slot);

    default void createItemWithPlayerName(int slot, String playerName) {}
}
