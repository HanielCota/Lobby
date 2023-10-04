package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.inventory.CompassInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompassInventoryFactory {
    public static CompassInventory createCompassInventory() {
        CompassInventory compassInventory = new CompassInventory(54, "Oque deseja jogar?");

        ItemStack lobbyItem = new ItemBuilder(Material.WORKBENCH)
                .setName("§aRetornar ao Lobby")
                .setLore("§eLore 1", "§eLore 2", "§eLore 3")
                .build();

        ItemStack factionsItem = new ItemBuilder(Material.TNT)
                .setName("§aFactions Legacy")
                .setLore("§eLore 1", "§eLore 2", "§eLore 3")
                .build();

        ItemStack bedWarsItem = new ItemBuilder(Material.BED)
                .setName("§aBedWars")
                .setLore("§eLore 1", "§eLore 2", "§eLore 3")
                .build();

        ItemStack rankUPItem = new ItemBuilder(Material.EXP_BOTTLE)
                .setName("§aRankUP")
                .setLore("§eLore 1", "§eLore 2", "§eLore 3")
                .build();



        compassInventory.setItemStack(10, lobbyItem);

        compassInventory.setItemStack(13, factionsItem);
        compassInventory.setItemStack(12, bedWarsItem);
        compassInventory.setItemStack(14, rankUPItem);

        return compassInventory;
    }
}
