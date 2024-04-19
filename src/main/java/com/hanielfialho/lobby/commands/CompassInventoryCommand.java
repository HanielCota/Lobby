package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.inventory.factories.CompassInventoryFactory;
import com.hanielfialho.lobby.inventory.factories.LobbyInventoryFactory;
import org.bukkit.entity.Player;

@CommandAlias("servermenu")
public class CompassInventoryCommand extends BaseCommand {

    @Default
    public void onCommand(Player player) {
        CompassInventoryFactory.createCompassInventory(player).open(player);
    }

    @Subcommand("lobby")
    public void onLobbyCommand(Player player) {
        new LobbyInventoryFactory().createLobbyInventory(player).open(player);
    }
}
