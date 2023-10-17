package com.hanielfialho.lobby.inventory.factories;

import com.hanielfialho.lobby.inventory.ProfileInventory;
import com.hanielfialho.lobby.utils.ItemBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.hanielfialho.lobby.LobbyPlugin.getInstance;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder // Add this annotation to allow constructors with more than 7 parameters
public class ProfileFactory {

    public static ProfileInventory createProfileInventory(Player player) {
        String entry = getInstance().getPlayerDateManager().getPlayerEntryDateFromCache(player.getName());

        String email = getInstance().getEmailDatabaseManager().getEmailForPlayer(player.getName());
        String country = getInstance().getCountryDatabaseManager().getCountryForPlayer(player.getName());
        String discord = getInstance().getDiscordDatabaseManager().getDiscordForPlayer(player.getName());
        String instagram = getInstance().getInstagramDatabaseManager().getInstagramForPlayer(player.getName());
        String phoneNumber = getInstance().getPlayerNumberManager().getPhoneNumberForPlayer(player.getName());
        String twitter = getInstance().getTwitterDatabaseManager().getTwitterForPlayer(player.getName());

        return createProfileInventory(
                entry != null ? entry : "entry não cadastrado.",
                email != null ? email : "Email não cadastrado.",
                country != null ? country : "Country não cadastrado.",
                discord != null ? discord : "Discord não cadastrado.",
                player.getName(),
                instagram != null ? instagram : "Instagram não cadastrado.",
                phoneNumber != null ? phoneNumber : "Telefone não cadastrado.",
                twitter != null ? twitter : "Twitter não cadastrado.");
    }

    private static ProfileInventory createProfileInventory(
            String playerEntryDate,
            String email,
            String country,
            String discord,
            String playerName,
            String instagram,
            String phone,
            String twitter) {
        ProfileInventory profileInventory = new ProfileInventory(54, "Player Profile");

        ItemStack timePlayedItem = new ItemBuilder(Material.WATCH)
                .setName("§aTempo jogado")
                .setLore("§7Primeiro login: " + playerEntryDate)
                .build();

        ItemStack playerHeadItem = new ItemBuilder(Material.SKULL_ITEM)
                .setName("§aSuas Informações")
                .setLore(
                        "§7Primeiro login: " + playerEntryDate,
                        "",
                        "§7Email: " + email,
                        "§7Country: " + country,
                        "§7Instagram: " + instagram,
                        "§7Celular: " + phone,
                        "§7Twitter: " + twitter,
                        "§7Discord: " + discord)
                .setSkullOwner(playerName)
                .build();

        profileInventory.setItemStack(10, timePlayedItem);
        profileInventory.setItemStack(11, playerHeadItem);

        return profileInventory;
    }
}
