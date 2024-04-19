package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.hanielfialho.lobby.LobbyPlugin;
import org.bukkit.entity.Player;

@CommandAlias("embaixadores")
public class EmbaixadoresCommand extends BaseCommand {

    @Default
    public void onCommand(Player player) {
        LobbyPlugin.sendPlayerToServer(player, "embaixadores");
        player.sendMessage("§eEstamos lhe redirecionando para o servidor dos embaixadores.");
    }

}
