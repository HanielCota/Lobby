package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.inventory.LobbyInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyInventoryFactory {

    public LobbyInventory createLobbyInventory(Player player) {

        LobbyInventory lobbyInventory = new LobbyInventory(3 * 9, "Escolher seu Lobby");

        ItemStack lobby = new ItemBuilder(Material.GREEN_DYE)
                .setDurability((short) 10)
                .setName("§aLobby 1")
                .setLore("§7Atualmente possuímos " + PlaceholderAPI.setPlaceholders(player, "%bungee_lobby% jogadores no Lobby #1."))
                .build();

        lobbyInventory.setItemStack(13, lobby);
        return lobbyInventory;

    }
}
