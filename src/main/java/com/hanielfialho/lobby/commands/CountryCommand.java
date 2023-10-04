package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.country.CountryDatabaseManager;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

@CommandAlias("country|pais")
@AllArgsConstructor
public class CountryCommand extends BaseCommand {

    private final CountryDatabaseManager countryManager;
    private final Logger logger = LoggerFactory.getLogger(CountryCommand.class);

    @Default
    private void onCountryCommand(Player player, String[] args) throws ExecutionException, InterruptedException {
        String playerName = player.getName();

        String existingCountry =
                countryManager.getCountryForPlayerAsync(playerName).get();
        if (existingCountry != null) {
            player.sendMessage("§aSeu país cadastrado é: " + capitalizeFirstLetter(existingCountry));
            return;
        }

        if (args.length < 1) {
            player.sendMessage("");
            player.sendMessage("§cUse: /country <país> para definir seu país.");
            player.sendMessage("");
            return;
        }

        String country = args[0];

        countryManager
                .setCountryForPlayerAsync(playerName, country)
                .thenRun(() -> {
                    player.sendMessage("§aPaís definido com sucesso: " + capitalizeFirstLetter(country));
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
                })
                .exceptionally(e -> {
                    logger.error("Erro ao definir o país do jogador", e);
                    player.sendMessage("§cErro ao definir o país. Por favor, tente novamente.");
                    return null;
                });
    }

    @Subcommand("delete")
    @CommandCompletion("@players")
    private void onDeleteCountryCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /country delete <nome do jogador>");
            return;
        }

        String targetPlayerName = args[0];

        if (targetPlayerName == null || targetPlayerName.isEmpty()) {
            player.sendMessage("§cNome de jogador inválido.");
            return;
        }

        countryManager.getCountryForPlayerAsync(targetPlayerName).thenAccept(existingCountry -> {
            if (existingCountry == null) {
                player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
                return;
            }

            countryManager
                    .deleteCountryForPlayerAsync(targetPlayerName)
                    .thenRun(() -> player.sendMessage("§aPaís de " + targetPlayerName + " removido com sucesso."))
                    .exceptionally(e -> {
                        logger.error("Erro ao remover o país de " + targetPlayerName, e);
                        player.sendMessage(
                                "§cErro ao remover o país de " + targetPlayerName + ". Por favor, tente novamente.");
                        return null;
                    });
        });
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
