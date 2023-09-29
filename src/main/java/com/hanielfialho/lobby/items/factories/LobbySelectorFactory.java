package com.hanielfialho.lobby.items.factories;

import com.hanielfialho.lobby.items.ItemFactory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LobbySelectorFactory implements ItemFactory {
    @Override
    public ItemStack createItem(int slot) {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName("§aSelecione um Lobby")
                .setLore("§7(Clique com o botão direito do mouse)", "", "§7para escolher entre um de nossos lobby's.")
                .build();
    }
}
