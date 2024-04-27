package dev.siea.collections;

import dev.siea.collections.commands.CollectionsCommand;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.managers.Manager;
import dev.siea.collections.messages.Messages;
import dev.siea.collections.storage.StorageManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Collections extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        StorageManager.init(this);
        saveResource("messages.yml", false);
        Messages.onEnable(this);
        Manager.enable(this);
        getServer().getPluginManager().registerEvents(new GUIWrapper(),this);
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
