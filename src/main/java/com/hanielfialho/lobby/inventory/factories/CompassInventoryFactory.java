package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.LobbyPlugin;
import com.hanielfialho.lobby.inventory.CompassInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class CompassInventoryFactory {

    private static final Map<Player, BukkitRunnable> iconAnimations = new HashMap<>();
    private static final Material[] bedMaterials = {Material.RED_BED, Material.BLUE_BED, Material.BLACK_BED, Material.YELLOW_BED, Material.PINK_BED};
    private static final Material[] tntMaterials = {Material.TNT, Material.TNT_MINECART, Material.CREEPER_SPAWN_EGG};
    private static final Map<Player, Integer> colorIndexes = new HashMap<>();
    private static final Map<Player, Integer> colorIndexesFac = new HashMap<>();

    public static CompassInventory createCompassInventory(Player player) {
        CompassInventory compassInventory = new CompassInventory(54, "Escolha um jogo");

        ItemStack lobbyItem = new ItemBuilder(Material.NETHER_STAR)
                .setName("§aLobby")
                .setLore(
                        "§7Retornar ao lobby principal",
                        "§7Clique aqui para voltar ao lobby"
                )
                .build();

        ItemStack buildItem = new ItemBuilder(Material.CRAFTING_TABLE)
                .setName("§eBuild Games")
                .setLore("§7Jogue BuildGames", "§7Construa as mais belas artes.", "",
                        "§7Com outros " + PlaceholderAPI.setPlaceholders(player, "%bungee_buildbattle% jogadores."))
                .build();

        ItemStack survivalItem = new ItemBuilder(Material.STONE_SWORD)
                .setName("§eSurvival")
                .setLore("§7Jogue Survival", "§7O survival como todos gostam.", "",
                        "§7Com outros " + PlaceholderAPI.setPlaceholders(player, "%bungee_survival% jogadores."))
                .build();

        compassInventory.setItemStack(15, buildItem);
        compassInventory.setItemStack(40, lobbyItem);
        compassInventory.setItemStack(22, survivalItem);

        BukkitRunnable animation = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack updatedBedWarsItem = createBedWarsItem(player);
                ItemStack updateFactionsItem = createFactionsItem(player);

                compassInventory.setItemStack(11, updatedBedWarsItem);
                compassInventory.setItemStack(13, updateFactionsItem);
            }
        };
        animation.runTaskTimer(LobbyPlugin.getInstance(), 20L, 20L);
        iconAnimations.put(player, animation);

        // Initial setup for bedWarsItem and factionsItem
        compassInventory.setItemStack(11, createBedWarsItem(player));
        compassInventory.setItemStack(13, createFactionsItem(player));

        return compassInventory;
    }

    private static ItemStack createBedWarsItem(Player player) {
        int colorIndex = colorIndexes.getOrDefault(player, 0);
        Material bedMaterial = bedMaterials[colorIndex];

        ItemStack bedWarsItem = new ItemBuilder(bedMaterial)
                .setName("§aBedWars")
                .setLore("§7Jogue BedWars", "§7Enfrente seus adversários", "",
                        "§7Com outros " + PlaceholderAPI.setPlaceholders(player, "%bungee_bedwars% jogadores."))
                .build();

        colorIndexes.put(player, (colorIndex + 1) % bedMaterials.length);

        return bedWarsItem;
    }

    private static ItemStack createFactionsItem(Player player) {
        int colorIndex = colorIndexesFac.getOrDefault(player, 0);
        Material facMaterial = tntMaterials[colorIndex];

        ItemStack factionsItem = new ItemBuilder(facMaterial)
                .setName("§aFactions Legacy")
                .setLore("§7Jogue Factions Legacy", "§7Entre na aventura das facções", "§cChegando no dia 30/04...", "",
                        "§7Com outros " + PlaceholderAPI.setPlaceholders(player, "%bungee_factions% jogadores."))
                .build();

        colorIndexesFac.put(player, (colorIndex + 1) % tntMaterials.length);

        return factionsItem;
    }


    public static void stopIconAnimation(Player player) {
        BukkitRunnable animation = iconAnimations.remove(player);
        if (animation != null) {
            animation.cancel();
        }
    }
}
