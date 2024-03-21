package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.inventory.LobbyInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LobbyInventoryFactory {

    public LobbyInventory createLobbyInventory() {

        LobbyInventory lobbyInventory = new LobbyInventory(3 * 9, "Escolher seu Lobby");

        ItemStack lobby = new ItemBuilder(Material.GREEN_DYE)
                .setDurability((short) 10)
                .setName("§aLobby 1")
                .setLore("§7Lobby 1", "§aDisponível")
                .build();

        ItemStack lobby1 = new ItemBuilder(Material.GREEN_DYE)
                .setName("§aLobby 2")
                .setDurability((short) 10)
                .setLore("§7Lobby 2", "§aDisponível")
                .build();

        lobbyInventory.setItemStack(10, lobby);
        lobbyInventory.setItemStack(11, lobby1);

        return lobbyInventory;
    }
}
