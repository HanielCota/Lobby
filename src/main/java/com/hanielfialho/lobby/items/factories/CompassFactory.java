package com.hanielfialho.lobby.items.factories;

import com.hanielfialho.lobby.items.ItemFactory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CompassFactory implements ItemFactory {

    @Override
    public ItemStack createItem(int slot) {
        return new ItemBuilder(Material.COMPASS)
                .setName("§aEscolha um Modo de Jogo")
                .setLore("§7(Clique com o botão direito do mouse)", "", "§7para escolher entre diversos modos de jogo.")
                .build();
    }
}
