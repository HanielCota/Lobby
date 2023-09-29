package com.hanielfialho.lobby.listeners;

import com.hanielfialho.lobby.LobbyPlugin;
import com.hanielfialho.lobby.listeners.players.*;
import org.bukkit.event.Listener;

public final class ListenerRegistry {

    private final ListenerManager<Listener> listenerManager;

    public ListenerRegistry(LobbyPlugin plugin) {
        listenerManager = new ListenerManager<>(plugin);
        addListeners();
        registerListeners();
    }

    private void addListeners() {
        listenerManager.addListener(new PlayerJoinListener());
        listenerManager.addListener(new PlayerQuitListener());
        listenerManager.addListener(new PlayerItemSetupListener());
        listenerManager.addListener(new PlayerFoodListener());
        listenerManager.addListener(new PlayerDeathListener());
    }

    public void registerListeners() {
        listenerManager.registerListeners();
    }
}
