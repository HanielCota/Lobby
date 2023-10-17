package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.inventory.CompassInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompassInventoryFactory {

    public static final String RANDOM_JOIN_SERVER_SKULL =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRkN2NjNGRjYTk4NmE1M2YxZDZiNTJhYWYzNzZkYzZhY2M3M2I4YjI4N2Y0MmRjOGZlZjU4MDhiYjVkNzYifX19";

    public static CompassInventory createCompassInventory() {
        CompassInventory compassInventory = new CompassInventory(54, "Escolha um jogo");

        ItemStack lobbyItem = new ItemBuilder(Material.NETHER_STAR)
                .setName("§aLobby")
                .setLore(
                        "§7Retornar ao lobby principal",
                        "§7Clique aqui para voltar ao lobby"
                )
                .build();

        ItemStack factionsItem = new ItemBuilder(Material.TNT)
                .setName("§aFactions Legacy")
                .setLore(
                        "§7Jogue Factions Legacy",
                        "§7Entre na aventura das facções"
                )
                .build();

        ItemStack bedWarsItem = new ItemBuilder(Material.BED)
                .setName("§aBedWars")
                .setLore(
                        "§7Jogue BedWars",
                        "§7Enfrente seus adversários"
                )
                .build();

        ItemStack rankUPItem = new ItemBuilder(Material.EXP_BOTTLE)
                .setName("§aRankUP")
                .setLore(
                        "§7Aprimore sua classificação",
                        "§7Suba de nível e se destaque"
                )
                .build();

        ItemStack joinRandomServer = new ItemBuilder(RANDOM_JOIN_SERVER_SKULL)
                .setName("§aServidor Aleatório")
                .setLore(
                        "§7Experimente um servidor aleatório",
                        "§7Uma escolha divertida!"
                )
                .build();

        compassInventory.setItemStack(19, lobbyItem);
        compassInventory.setItemStack(22, factionsItem);
        compassInventory.setItemStack(21, bedWarsItem);
        compassInventory.setItemStack(23, rankUPItem);
        compassInventory.setItemStack(49, joinRandomServer);

        return compassInventory;
    }
}
