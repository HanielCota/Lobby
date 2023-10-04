package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.player.PlayerNumberManager;
import com.hanielfialho.lobby.utils.validations.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandAlias("celular")
@AllArgsConstructor
public class PhoneNumberCommand extends BaseCommand {

    private final PlayerNumberManager numberManager;
    private final Logger logger = LoggerFactory.getLogger(PhoneNumberCommand.class);

    @Default
    private void onPhoneNumberCommand(Player player, String[] args) {
        String playerName = player.getName();

        numberManager
                .getPhoneNumberForPlayerAsync(playerName)
                .thenAccept(phoneNumber -> {
                    if (phoneNumber != null) {

                        String formattedPhoneNumber =
                                "(" + phoneNumber.substring(0, 2) + ") " + phoneNumber.substring(2);

                        player.sendMessage("§aSeu número de celular é: " + formattedPhoneNumber);
                        return;
                    }

                    if (args.length < 1) {

                        player.sendMessage(new String[] {
                            "",
                            "§cVocê não possui número de celular cadastrado.",
                            "§cUse /celular <número> para cadastrar seu número.",
                            ""
                        });
                        return;
                    }

                    String newPhoneNumber = args[0];
                    PhoneNumberValidator validator = new PhoneNumberValidator();

                    if (!validator.isValidPhoneNumber(newPhoneNumber)) {
                        player.sendMessage("§cNúmero de celular inválido.");
                        player.sendMessage("§cCertifique-se de inserir um número válido com DDD brasileiro.");
                        player.sendMessage("");
                        player.sendMessage("§cLembre-se que são 9 dígitos geralmente.");
                        return;
                    }

                    numberManager
                            .setPhoneNumberForPlayerAsync(playerName, newPhoneNumber)
                            .thenAccept(result ->
                                    player.sendMessage("§aNúmero de celular definido com sucesso: " + newPhoneNumber))
                            .exceptionally(e -> {
                                logger.error("Erro ao definir número de celular do jogador", e);
                                return null;
                            });
                })
                .exceptionally(e -> {
                    logger.error("Erro ao obter número de celular do jogador", e);
                    return null;
                });
    }

    @Subcommand("delete")
    private void onDeletePhoneCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /phone delete <nome do jogador>");
            return;
        }

        String targetPlayerName = args[0];

        if (targetPlayerName == null || targetPlayerName.isEmpty()) {
            player.sendMessage("§cNome de jogador inválido.");
            return;
        }

        numberManager.getPhoneNumberForPlayerAsync(targetPlayerName).thenAccept(existingPhone -> {
            if (existingPhone == null) {
                player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
                return;
            }

            numberManager
                    .deletePhoneForPlayerAsync(targetPlayerName)
                    .thenRun(() -> player.sendMessage("§aTelefone de " + targetPlayerName + " removido com sucesso."))
                    .exceptionally(e -> {
                        logger.error("Erro ao remover o telefone de " + targetPlayerName, e);
                        player.sendMessage("§cErro ao remover o telefone de " + targetPlayerName
                                + ". Por favor, tente novamente.");
                        return null;
                    });
        });
    }
}
