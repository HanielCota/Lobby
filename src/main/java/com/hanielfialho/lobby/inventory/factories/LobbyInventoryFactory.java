package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.inventory.LobbyInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LobbyInventoryFactory {

    public LobbyInventory createLobbyInventory() {

        LobbyInventory lobbyInventory = new LobbyInventory(3 * 9, "Escolher seu Lobby");

        ItemStack lobby = new ItemBuilder(Material.INK_SACK)
                .setDurability((short) 10)
                .setLore("§7Lobby 1", "§aDisponível")
                .build();

        ItemStack lobby1 = new ItemBuilder(Material.INK_SACK)
                .setDurability((short) 10)
                .setLore("§7Lobby 2", "§aDisponível")
                .build();

        lobbyInventory.setItemStack(10, lobby);
        lobbyInventory.setItemStack(11, lobby1);

        return lobbyInventory;
    }
}
