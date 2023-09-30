package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.hanielfialho.lobby.manager.twitter.TwitterDatabaseManager;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandAlias("twitter")
@AllArgsConstructor
public class TwitterCommand extends BaseCommand {

    private final TwitterDatabaseManager twitterManager;
    private final Logger logger = LoggerFactory.getLogger(TwitterCommand.class);

    @Default
    private void onTwitterCommand(Player player, String[] args) {
        String playerName = player.getName();

        twitterManager.getTwitterForPlayerAsync(playerName).thenAccept(existingTwitter -> {
            if (existingTwitter != null) {
                player.sendMessage("§aSeu usuário do Twitter cadastrado é: @" + existingTwitter);
                return;
            }

            if (args.length < 1) {
                player.sendMessage("");
                player.sendMessage("§cUse: /twitter <usuário> para cadastrar ou visualizar seu usuário do Twitter.");
                player.sendMessage("");
                return;
            }

            String twitterUsername = args[0];

            twitterManager
                    .setTwitterForPlayerAsync(playerName, twitterUsername)
                    .thenRun(() -> {
                        player.sendMessage("§aUsuário do Twitter definido com sucesso: @" + twitterUsername);
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
                    })
                    .exceptionally(e -> {
                        logger.error("Erro ao definir o usuário do Twitter do jogador", e);
                        player.sendMessage("§cErro ao definir o usuário do Twitter. Por favor, tente novamente.");
                        return null;
                    });
        });
    }
}