package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.discord.DiscordDatabaseManager;
import lombok.AllArgsConstructor;
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

        discordManager.getDiscordForPlayerAsync(playerName).thenAccept(existingDiscord -> {
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

            discordManager
                    .setDiscordForPlayerAsync(playerName, discordUsername)
                    .thenRun(() -> {
                        player.sendMessage("§aUsuário do Discord definido com sucesso: " + discordUsername);
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
                    })
                    .exceptionally(e -> {
                        logger.error("Erro ao definir o usuário do Discord do jogador", e);
                        player.sendMessage("§cErro ao definir o usuário do Discord. Por favor, tente novamente.");
                        return null;
                    });
        });
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

        discordManager.getDiscordForPlayerAsync(targetPlayerName).thenAccept(existingDiscord -> {
            if (existingDiscord == null) {
                player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
                return;
            }

            discordManager
                    .deleteDiscordForPlayerAsync(targetPlayerName)
                    .thenRun(() -> player.sendMessage(
                            "§aUsuário do Discord de " + targetPlayerName + " removido com sucesso."))
                    .exceptionally(e -> {
                        logger.error("Erro ao remover o usuário do Discord de " + targetPlayerName, e);
                        player.sendMessage("§cErro ao remover o usuário do Discord de " + targetPlayerName
                                + ". Por favor, tente novamente.");
                        return null;
                    });
        });
    }
}
