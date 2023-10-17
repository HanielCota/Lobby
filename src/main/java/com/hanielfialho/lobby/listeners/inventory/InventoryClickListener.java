package com.hanielfialho.lobby.listeners.inventory;

import com.hanielfialho.lobby.items.ItemAction;
import com.hanielfialho.lobby.items.actions.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {
    private final Map<Material, ItemAction> itemActions = new HashMap<>();

    public InventoryClickListener() {
        // Associe os itens específicos às ações correspondentes

        itemActions.put(Material.COMPASS, new CompassAction());
        itemActions.put(Material.SKULL_ITEM, new PlayerHeadAction());
        itemActions.put(Material.RAW_FISH, new FishAction());
        itemActions.put(Material.INK_SACK, new DyeAction());
        itemActions.put(Material.NETHER_STAR, new NetherStartAction());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clickedItem = player.getInventory().getItemInHand();

        if (clickedItem != null
                && !clickedItem.getType().equals(Material.AIR)
                && itemActions.containsKey(clickedItem.getType())) {

            ItemAction action = itemActions.get(clickedItem.getType());
            action.execute(player);

            event.setCancelled(true);
        }
    }
}
