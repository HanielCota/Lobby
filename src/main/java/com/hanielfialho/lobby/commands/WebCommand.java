package com.hanielfialho.lobby.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.hanielfialho.lobby.utils.ClickMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("web|site")
public class WebCommand extends BaseCommand {

    @Default
    public void onCommand(Player player) {
        new ClickMessage("§eClique aqui para acessar nosso Site oficial.").click(ClickEvent.Action.OPEN_URL, "https://floruitmc.com/").send(player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 10.0F);

    }
}
