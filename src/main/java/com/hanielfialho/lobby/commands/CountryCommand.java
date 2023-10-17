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

@AllArgsConstructor
@CommandAlias("country|pais")
public class CountryCommand extends BaseCommand {

    private final CountryDatabaseManager countryManager;
    private final Logger logger = LoggerFactory.getLogger(CountryCommand.class);

    @Default
    private void onCountryCommand(Player player, String[] args) {
        String playerName = player.getName();

        String existingCountry = countryManager.getCountryForPlayer(playerName);
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

        countryManager.setCountryForPlayer(playerName, country);

        player.sendMessage("§aPaís definido com sucesso: " + capitalizeFirstLetter(country));
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
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

        String existingCountry = countryManager.getCountryForPlayer(targetPlayerName);
        if (existingCountry == null) {
            player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
            return;
        }

        countryManager.deleteCountryForPlayer(targetPlayerName);

        player.sendMessage("§aPaís de " + targetPlayerName + " removido com sucesso.");
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
