package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.discord.DiscordDatabaseManager;
import com.hanielfialho.lobby.utils.ClickMessage;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandAlias("discord")
@AllArgsConstructor
public class DiscordCommand extends BaseCommand {

    private final DiscordDatabaseManager discordManager;
    private final Logger logger = LoggerFactory.getLogger(DiscordCommand.class);

    @Default
    private void onDiscordCommand(Player player, String[] args) {
        String playerName = player.getName();

        String existingDiscord = discordManager.getDiscordForPlayer(playerName);
        if (existingDiscord != null) {
            player.sendMessage("§aSeu usuário do Discord cadastrado é: " + existingDiscord);
            return;
        }

        if (args.length < 1) {
            player.sendMessage("");
            player.sendMessage("§cUse: /discord <usuário> para cadastrar ou visualizar seu usuário do Discord.");
            player.sendMessage("");
            return;
        }

        String discordUsername = args[0];

        discordManager.setDiscordForPlayer(playerName, discordUsername);

        player.sendMessage("§aUsuário do Discord definido com sucesso: " + discordUsername);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10F, 1F);
    }

    @Subcommand("delete")
    private void onDeleteDiscordCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /discord delete <nome do jogador>");
            return;
        }

        String targetPlayerName = args[0];

        if (targetPlayerName == null || targetPlayerName.isEmpty()) {
            player.sendMessage("§cNome de jogador inválido.");
            return;
        }

        String existingDiscord = discordManager.getDiscordForPlayer(targetPlayerName);
        if (existingDiscord == null) {
            player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
            return;
        }

        discordManager.deleteDiscordForPlayer(targetPlayerName);

        player.sendMessage("§aUsuário do Discord de " + targetPlayerName + " removido com sucesso.");
    }

    @Subcommand("ir")
    private void onRedirectDiscord(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 10.0F);
        new ClickMessage("§eClique aqui para acessar nosso Discord oficial.").click(ClickEvent.Action.OPEN_URL, "https://floruitmc.com/discord").send(player);
    }
}
