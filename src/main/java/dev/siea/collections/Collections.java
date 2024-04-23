package dev.siea.collections;

import dev.siea.collections.api.CollectionsAPI;
import dev.siea.collections.commands.CollectionsCommand;
import dev.siea.collections.managers.Manager;
import dev.siea.collections.messages.Messages;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Collections extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        saveResource("messages.yml", false);
        Messages.onEnable(this);
        Manager.enable(this);
        getCommand("collections").setExecutor(new CollectionsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
