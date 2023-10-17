package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
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

        String existingTwitter = twitterManager.getTwitterForPlayer(playerName);
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

        twitterManager.setTwitterForPlayer(playerName, twitterUsername);

        player.sendMessage("§aUsuário do Twitter definido com sucesso: @" + twitterUsername);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
    }

    @Subcommand("delete")
    private void onDeleteTwitterCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /twitter delete <nome do jogador>");
            return;
        }

        String targetPlayerName = args[0];

        if (targetPlayerName == null || targetPlayerName.isEmpty()) {
            player.sendMessage("§cNome de jogador inválido.");
            return;
        }

        String existingTwitter = twitterManager.getTwitterForPlayer(targetPlayerName);
        if (existingTwitter == null) {
            player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
            return;
        }

        twitterManager.deleteTwitterForPlayer(targetPlayerName);

        player.sendMessage("§aTwitter de " + targetPlayerName + " removido com sucesso.");
    }
}
