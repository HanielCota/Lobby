package com.hanielfialho.lobby.items.factories;

import com.hanielfialho.lobby.items.ItemFactory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VisibilityControlFactory implements ItemFactory {

    @Override
    public ItemStack createItem(int slot) {
        return new ItemBuilder(Material.GREEN_DYE)
                .setName("§aEsconder")
                .setLore(
                        "§7(Clique com o botão direito do mouse)",
                        "",
                        "§7para ativar ou desativar visibilidade de jogadores.")
                .setDurability((short) 10)
                .build();
    }
}
