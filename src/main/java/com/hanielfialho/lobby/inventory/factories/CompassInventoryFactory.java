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
    private static final Material[] bedMaterials = {
        Material.RED_BED, Material.BLUE_BED, Material.BLACK_BED, Material.YELLOW_BED, Material.PINK_BED
    };
    private static final Material[] tntMaterials = {Material.TNT, Material.TNT_MINECART, Material.CREEPER_SPAWN_EGG};
    private static final Map<Player, Integer> colorIndexes = new HashMap<>();
    private static final Map<Player, Integer> colorIndexesFac = new HashMap<>();

    public static CompassInventory createCompassInventory(Player player) {
        CompassInventory compassInventory = new CompassInventory(54, "Escolha um jogo");

        ItemStack buildItem = new ItemBuilder(Material.CRAFTING_TABLE)
                .setName("§b§lBUILD GAMES")
                .setLore(
                        "",
                        "§eFaça a sua melhor construção sem ter medo de errar,",
                        "§econstrua, vote, e se torne o construtor",
                        "§emais fora da curva que conseguir.",
                        "",
                        "§bClique aqui para jogar!",
                        "§d" + PlaceholderAPI.setPlaceholders(player, "%bungee_buildbattle% jogando agora"))
                .build();

        // Atualizando nome e lore do item 'Survival'
        ItemStack survivalItem = new ItemBuilder(Material.STONE_SWORD)
                .setName("§a§lSURVIVAL")
                .setLore(
                        "",
                        "§eQuanto mais explorar, mais desafiador vai ficar!",
                        "§eEntre e desfrute do seu modo de jogo querido.",
                        "",
                        "§bClique aqui para jogar!",
                        "§d" + PlaceholderAPI.setPlaceholders(player, "%bungee_survival% jogando agora"))
                .build();

        ItemStack lobbyItem = new ItemBuilder(Material.NETHER_STAR)
                .setName("§a§lHUB")
                .setLore("§7Clique para voltar ao Hub.")
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
        ItemStack bedWarsItem = new ItemBuilder(
                        Material.RED_BED) // Pode ser necessário atualizar o material conforme a lógica de cores.
                .setName("§c§lBEDWARS")
                .setLore(
                        "",
                        "§eO modo de jogo mais famoso do mundo",
                        "§ejá está aqui na §f§lFloruit MC§r§e,",
                        "§econquiste a ilha adversária, quebre a cama",
                        "§ee se torne o rei do §c§lBEDWARS§r§e.",
                        "",
                        "§bClique aqui para jogar!",
                        "§d" + PlaceholderAPI.setPlaceholders(player, "%bungee_bedwars% jogando agora"))
                .build();

        // A lógica para atualizar o índice da cor permanece a mesma
        int colorIndex = colorIndexes.getOrDefault(player, 0);
        colorIndexes.put(player, (colorIndex + 1) % bedMaterials.length);

        return bedWarsItem;
    }

    private static ItemStack createFactionsItem(Player player) {
        ItemStack factionsItem = new ItemBuilder(Material.TNT)
                .setName("§d§lFACTIONS LEGACY")
                .setLore(
                        "",
                        "§eA mistura perfeita de combate e novidade,",
                        "§eseja bem vindo ao Factions! Junte seus amigos",
                        "§ee prepare-se para a batalha!",
                        "",
                        "§bClique aqui para jogar!",
                        "§d" + PlaceholderAPI.setPlaceholders(player, "%bungee_factions% jogando agora"))
                .build();

        // A lógica para atualizar o índice da cor permanece a mesma
        int colorIndex = colorIndexesFac.getOrDefault(player, 0);
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
