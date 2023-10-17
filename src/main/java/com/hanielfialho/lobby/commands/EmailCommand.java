package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.hanielfialho.lobby.manager.email.EmailDatabaseManager;
import com.hanielfialho.lobby.utils.ClickMessage;
import com.hanielfialho.lobby.utils.validations.EmailValidator;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.hanielfialho.lobby.utils.validations.EmailValidator.isValidEmail;

@CommandAlias("email")
@AllArgsConstructor
public class EmailCommand extends BaseCommand {

    private final EmailDatabaseManager emailManager;
    private final Logger logger = LoggerFactory.getLogger(EmailCommand.class);

    @Default
    private void onEmailCommand(Player player, String[] args) {
        String playerName = player.getName();

        String existingEmail = emailManager.getEmailForPlayer(playerName);
        if (existingEmail != null) {
            player.sendMessage("§aSeu e-mail cadastrado é: " + existingEmail);
            return;
        }

        if (args.length < 1) {
            player.sendMessage("");
            player.sendMessage("§cUse: /email <email> para cadastrar ou visualizar seu e-mail.");
            new ClickMessage("§eClique ")
                    .then("§e§lAQUI")
                    .click(Action.RUN_COMMAND, "/email domain")
                    .then("§e para visualizar a lista de emails aceitos.")
                    .send(player);
            player.sendMessage("");
            return;
        }

        String email = args[0];

        if (!isValidEmail(email)) {
            player.sendMessage("§cE-mail inválido. Por favor, insira um e-mail válido.");
            return;
        }

        emailManager.setEmailForPlayer(playerName, email);

        player.sendMessage("§aE-mail definido com sucesso para: " + email);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
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
        player.sendMessage("\n§eUtilize um desses domínios para cadastrar o seu e-mail.");
    }

    @Subcommand("delete")
    private void onDeleteEmailCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /email delete <nome do jogador>");
            return;
        }

        String targetPlayerName = args[0];

        if (targetPlayerName == null || targetPlayerName.isEmpty()) {
            player.sendMessage("§cNome de jogador inválido.");
            return;
        }

        String existingEmail = emailManager.getEmailForPlayer(targetPlayerName);
        if (existingEmail == null) {
            player.sendMessage("§cO jogador " + targetPlayerName + " não foi encontrado no banco de dados.");
            return;
        }

        emailManager.deleteEmailForPlayer(targetPlayerName);

        player.sendMessage("§aE-mail de " + targetPlayerName + " removido com sucesso.");
    }
}
