package com.hanielfialho.lobby.listeners.inventory;

import com.hanielfialho.lobby.LobbyPlugin;
import com.hanielfialho.lobby.inventory.ProfileInventory;
import com.hanielfialho.lobby.inventory.factories.ProfileFactory;
import com.hanielfialho.lobby.items.ItemAction;
import com.hanielfialho.lobby.items.actions.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {
    private final Map<Material, ItemAction> itemActions = new HashMap<>();

    public InventoryClickListener() {
        initializeItemActions();
    }

    private void initializeItemActions() {
        itemActions.put(Material.COMPASS, new CompassAction());
        itemActions.put(Material.PLAYER_HEAD, new PlayerHeadAction());
        itemActions.put(Material.PUFFERFISH, new FishAction());
        itemActions.put(Material.RED_DYE, new DyeAction());
        itemActions.put(Material.GREEN_DYE, new DyeAction());
        itemActions.put(Material.NETHER_STAR, new NetherStartAction());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clickedItem = player.getInventory().getItemInMainHand();

        if (!clickedItem.getType().equals(Material.AIR) && itemActions.containsKey(clickedItem.getType())) {
            ItemAction action = itemActions.get(clickedItem.getType());
            action.execute(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 10.0F);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractHead(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clickedItem = player.getInventory().getItemInMainHand();

        if (isPlayerHeadWithDisplayName(clickedItem)) {
            ProfileInventory profileInventory = ProfileFactory.createProfileInventory(player);
            profileInventory.open(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 10.0F);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player player) {
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.TNT) {
                LobbyPlugin.sendPlayerToServer(player, "factions");
                event.setCancelled(true);
                return;
            }

            if (clickedItem != null && clickedItem.getType() == Material.RED_BED) {
                LobbyPlugin.sendPlayerToServer(player, "bedwars");
                event.setCancelled(true);
            }
        }
    }

    private boolean isPlayerHeadWithDisplayName(ItemStack itemStack) {
        return itemStack.getType() == Material.PLAYER_HEAD && hasDisplayName(itemStack);
    }

    private boolean hasDisplayName(ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§aPerfil");
    }
}
