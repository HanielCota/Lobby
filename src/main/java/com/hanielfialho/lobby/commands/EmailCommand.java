package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.email.EmailDatabaseManager;
import com.hanielfialho.lobby.utils.ClickMessage;
import com.hanielfialho.lobby.utils.EmailValidator;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.hanielfialho.lobby.utils.EmailValidator.isValidEmail;

@CommandAlias("email")
@AllArgsConstructor
public class EmailCommand extends BaseCommand {

    private final EmailDatabaseManager emailManager;
    private final Logger logger = LoggerFactory.getLogger(EmailCommand.class);

    @Default
    private void onEmailCommand(Player player, String[] args) {
        if (args.length < 1) {

            player.sendMessage("");
            player.sendMessage("§cUso correto: /email <email> para cadastrar um de seus emails.");

            new ClickMessage("§eClique ")
                    .then("§e§lAQUI")
                    .click(Action.RUN_COMMAND, "/email domain")
                    .then("§e para visualizar a lista de emails aceitos.")
                    .send(player);

            player.sendMessage("");
            return;
        }

        String email = args[0];
        String playerName = player.getName();

        emailManager.getEmailForPlayerAsync(playerName).thenAccept(existingEmail -> {
            if (existingEmail != null) {
                player.sendMessage("§cVocê já tem um e-mail definido: " + existingEmail);
                return;
            }

            if (!isValidEmail(email)) {
                player.sendMessage("§cE-mail inválido. Por favor, insira um e-mail válido.");
                return;
            }

            emailManager.setEmailForPlayerAsync(playerName, email)
                    .thenRun(() -> {
                        player.sendMessage("§aE-mail definido com sucesso para: " + email);
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
                    })
                    .exceptionally(e -> {
                        logger.error("Erro ao definir o e-mail do jogador", e);
                        player.sendMessage("§cErro ao definir o e-mail. Por favor, tente novamente.");
                        return null;
                    });
        });
    }

    @Subcommand("domain")
    private void onEmailDomainCommand(Player player, String[] args) {
        List<String> validEmailDomains = EmailValidator.getValidEmailDomains();

        if (validEmailDomains.isEmpty()) {
            player.sendMessage("§cNenhum domínio de e-mail válido encontrado.");
            return;
        }

        ClickMessage clickMessage = new ClickMessage("§aLista de Domínios Válidos:\n");

        for (String validEmailDomain : validEmailDomains) {
            clickMessage.then("\n§e" + validEmailDomain);
        }

        clickMessage.send(player);
    }

}
