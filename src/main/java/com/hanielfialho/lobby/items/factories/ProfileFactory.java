package com.hanielfialho.lobby.items.factories;

import com.hanielfialho.lobby.items.ItemFactory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ProfileFactory implements ItemFactory {

    private final String playerName;

    @Override
    public ItemStack createItem(int slot) {
        return new ItemBuilder(Material.SKULL_ITEM)
                .setName("§aPerfil")
                .setLore("§7(Clique com o botão direito do mouse)", "", "§7para configurar suas preferências.")
                .setSkullOwner(playerName)
                .build();
    }
}
