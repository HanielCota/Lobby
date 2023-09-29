package com.hanielfialho.lobby.items.factories;

import com.hanielfialho.lobby.items.ItemFactory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GadgetsFactory implements ItemFactory {

    @Override
    public ItemStack createItem(int slot) {
        return new ItemBuilder(Material.RAW_FISH)
                .setName("§aGadgets")
                .setLore("§7(Clique com o botão direito do mouse)", "", "§7para comprar seus gadgets e se divertir.")
                .setDurability((short) 3)
                .build();
    }
}
