package com.hanielfialho.lobby.listeners;

import com.hanielfialho.lobby.LobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class ListenerManager<T extends Listener> {
    private final List<T> listeners = new ArrayList<>();
    private final LobbyPlugin plugin;

    public ListenerManager(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }

    public void registerListeners() {
        if (listeners.isEmpty()) {
            Bukkit.getLogger().info("§cThe list is empty, so no listeners have been registered.");
            return;
        }

        listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));

    }
}
