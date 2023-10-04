package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.instagram.InstagramDatabaseManager;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandAlias("instagram")
@AllArgsConstructor
public class InstagramCommand extends BaseCommand {

    private final InstagramDatabaseManager instagramManager;
    private final Logger logger = LoggerFactory.getLogger(InstagramCommand.class);

    @Default
    private void onInstagramCommand(Player player, String[] args) {
        String playerName = player.getName();

        instagramManager.getInstagramForPlayerAsync(playerName).thenAccept(existingInstagram -> {
            if (existingInstagram != null) {
                player.sendMessage("§aSeu usuário do Instagram cadastrado é: @" + existingInstagram);
                return;
            }

            if (args.length < 1) {
                player.sendMessage("");
                player.sendMessage(
                        "§cUse: /instagram <usuário> para cadastrar ou visualizar seu usuário do Instagram.");
                player.sendMessage("");
                return;
            }

            String instagramUsername = args[0];

            instagramManager
                    .setInstagramForPlayerAsync(playerName, instagramUsername)
                    .thenRun(() -> {
                        player.sendMessage("§aUsuário do Instagram definido com sucesso: @" + instagramUsername);
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
                    })
                    .exceptionally(e -> {
                        logger.error("Erro ao definir o usuário do Instagram do jogador", e);
                        player.sendMessage("§cErro ao definir o usuário do Instagram. Por favor, tente novamente.");
                        return null;
                    });
        });
    }

    @Subcommand("delete")
    private void onDeleteInstagramCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /instagram delete <nome do jogador>");
            return;
        }

        String targetPlayerName = args[0];

        if (targetPlayerName == null || targetPlayerName.isEmpty()) {
            player.sendMessage("§cNome de jogador inválido.");
            return;
        }

        instagramManager.getInstagramForPlayerAsync(targetPlayerName).thenAccept(existingInstagram -> {
            if (existingInstagram == null) {
                player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
                return;
            }

            instagramManager
                    .deleteInstagramForPlayerAsync(targetPlayerName)
                    .thenRun(() -> player.sendMessage("§aInstagram de " + targetPlayerName + " removido com sucesso."))
                    .exceptionally(e -> {
                        logger.error("Erro ao remover o Instagram de " + targetPlayerName, e);
                        player.sendMessage("§cErro ao remover o Instagram de " + targetPlayerName
                                + ". Por favor, tente novamente.");
                        return null;
                    });
        });
    }
}
