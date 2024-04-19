package com.hanielfialho.lobby;

import co.aikar.commands.PaperCommandManager;
import com.hanielfialho.lobby.commands.*;
import com.hanielfialho.lobby.database.DatabaseManager;
import com.hanielfialho.lobby.database.DatabaseTableCreator;
import com.hanielfialho.lobby.listeners.ListenerRegistry;
import com.hanielfialho.lobby.manager.country.CountryDatabaseManager;
import com.hanielfialho.lobby.manager.discord.DiscordDatabaseManager;
import com.hanielfialho.lobby.manager.email.EmailDatabaseManager;
import com.hanielfialho.lobby.manager.instagram.InstagramDatabaseManager;
import com.hanielfialho.lobby.manager.player.PlayerDateManager;
import com.hanielfialho.lobby.manager.player.PlayerNumberManager;
import com.hanielfialho.lobby.manager.twitter.TwitterDatabaseManager;
import com.hanielfialho.lobby.utils.ConfigUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

@Getter
public final class LobbyPlugin extends JavaPlugin {

    // Data Managers
    private DatabaseTableCreator tableCreator;
    private ConfigUtils databaseConfig;
    private DatabaseManager databaseManager;
    private ListenerRegistry listenerRegistry;
    private EmailDatabaseManager emailDatabaseManager;
    private PlayerNumberManager playerNumberManager;
    private TwitterDatabaseManager twitterDatabaseManager;
    private InstagramDatabaseManager instagramDatabaseManager;
    private DiscordDatabaseManager discordDatabaseManager;
    private CountryDatabaseManager countryDatabaseManager;
    private PlayerDateManager playerDateManager;

    public static LobbyPlugin getInstance() {
        return JavaPlugin.getPlugin(LobbyPlugin.class);
    }

    public static void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        } catch (Exception var4) {
            player.sendMessage(ChatColor.RED + "Error when trying to connect to " + server);
        }
    }

    @Override
    public void onEnable() {
        initializeDatabase();
        createTables();
        initializeEmailManager();
        initializePlayerNumberManager();
        initializeTwitterManager();
        initializeInstagramManager();
        initializeDiscordManager();
        initializeCountyManager();
        initializePlayerDateManager();
        initializeCommands();
        initializeListeners();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void initializeDatabase() {
        databaseConfig = new ConfigUtils(this, "database.yml");
        tableCreator = new DatabaseTableCreator(this);
        databaseManager = createDatabaseManager();
    }

    private DatabaseManager createDatabaseManager() {
        String jdbcUrl = databaseConfig.getString("database.jdbcUrl");
        String username = databaseConfig.getString("database.username");
        String password = databaseConfig.getString("database.password");
        return new DatabaseManager(jdbcUrl, username, password);
    }

    private void initializeListeners() {
        listenerRegistry = new ListenerRegistry(this);
    }

    private void initializeEmailManager() {
        emailDatabaseManager = new EmailDatabaseManager(this);
    }

    private void initializePlayerNumberManager() {
        playerNumberManager = new PlayerNumberManager(this);
    }

    private void initializeTwitterManager() {
        twitterDatabaseManager = new TwitterDatabaseManager(this);
    }

    private void initializeInstagramManager() {
        instagramDatabaseManager = new InstagramDatabaseManager(this);
    }

    private void initializeDiscordManager() {
        discordDatabaseManager = new DiscordDatabaseManager(this);
    }

    private void initializeCountyManager() {
        countryDatabaseManager = new CountryDatabaseManager(this);
    }

    private void initializePlayerDateManager() {
        playerDateManager = new PlayerDateManager(this);
    }

    private void initializeCommands() {
        PaperCommandManager commandManager = createCommandManager();
        registerCommands(commandManager);
    }

    private PaperCommandManager createCommandManager() {
        return new PaperCommandManager(this);
    }

    private void registerCommands(PaperCommandManager commandManager) {
        commandManager.registerCommand(new EmailCommand(emailDatabaseManager));
        commandManager.registerCommand(new PhoneNumberCommand(playerNumberManager));
        commandManager.registerCommand(new TwitterCommand(twitterDatabaseManager));
        commandManager.registerCommand(new InstagramCommand(instagramDatabaseManager));
        commandManager.registerCommand(new DiscordCommand(discordDatabaseManager));
        commandManager.registerCommand(new CountryCommand(countryDatabaseManager));
        commandManager.registerCommand(new WebCommand());
        commandManager.registerCommand(new CompassInventoryCommand());
        commandManager.registerCommand(new EmbaixadoresCommand());
    }

    private void createTables() {
        tableCreator.createTable(
                "lobby",
                "(id INT AUTO_INCREMENT PRIMARY KEY, player_name VARCHAR(255)," + " entry_date VARCHAR(255), "
                        + "last_exit_date VARCHAR(255), "
                        + "player_language VARCHAR(255), "
                        + "player_country VARCHAR(255), "
                        + "player_email VARCHAR(255), "
                        + "phone_number VARCHAR(255), "
                        + "discord_username VARCHAR(255), "
                        + "instagram_username VARCHAR(255), "
                        + "twitter_username VARCHAR(255))");
    }
}
