package com.hanielfialho.lobby;

import com.hanielfialho.lobby.listeners.ListenerRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        initializeListeners();
    }

    private void initializeListeners() {
        ListenerRegistry listenerRegistry = new ListenerRegistry(this);
        listenerRegistry.registerListeners();
    }
}
