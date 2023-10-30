package com.hanielfialho.lobby.items.actions;

import com.hanielfialho.lobby.inventory.LobbyInventory;
import com.hanielfialho.lobby.inventory.factories.LobbyInventoryFactory;
import com.hanielfialho.lobby.items.ItemAction;
import org.bukkit.entity.Player;

public class NetherStartAction implements ItemAction {
    @Override
    public void execute(Player player) {
        LobbyInventory lobbyInventory = new LobbyInventoryFactory().createLobbyInventory();

        lobbyInventory.open(player);
    }
}
