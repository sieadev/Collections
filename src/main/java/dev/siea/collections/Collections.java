package dev.siea.collections;

import dev.siea.collections.commands.CollectionsCommand;
import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.listeners.PlayerConnectionListeners;
import dev.siea.collections.managers.Manager;
import dev.siea.collections.messages.Messages;
import dev.siea.collections.storage.StorageManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Collections extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        //Save config files
        saveDefaultConfig();
        saveResource("messages.yml", false);

        plugin = this;
        StorageManager.init(this);
        Messages.onEnable(this);
        Manager.enable(this);
        getServer().getPluginManager().registerEvents(new GUIWrapper(),this);
        Objects.requireNonNull(getCommand("collections")).setExecutor(new CollectionsCommand());
        getServer().getPluginManager().registerEvents(new PlayerConnectionListeners(),this);
        getServer().getPluginManager().registerEvents(new CreationManager(),this);
    }

    @Override
    public void onDisable() {
        try{
            Manager.disable();
            StorageManager.shutdown();
        } catch(Exception e){
            return;
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
