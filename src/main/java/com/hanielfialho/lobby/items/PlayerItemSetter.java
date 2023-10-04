package com.hanielfialho.lobby.items;

import com.hanielfialho.lobby.items.ItemFactory;
import com.hanielfialho.lobby.items.factories.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerItemSetter {

    public static void setInitialItems(Player player) {

        String playerName = player.getName();

        ItemFactory[] itemFactories = {
            new CompassFactory(),
            new ProfileFactory(playerName),
            new GadgetsFactory(),
            new VisibilityControlFactory(),
            new LobbySelectorFactory()
        };

        int[] itemSlots = {0, 1, 4, 7, 8}; // Defina os slots desejados aqui

        player.getInventory().clear();

        for (int i = 0; i < itemFactories.length; i++) {
            int slot = itemSlots[i];
            player.getInventory().setItem(slot, itemFactories[i].createItem(slot));
        }
    }
}