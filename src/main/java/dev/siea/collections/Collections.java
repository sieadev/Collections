package dev.siea.collections;

import dev.siea.collections.commands.CollectionsCommand;
import dev.siea.collections.commands.DeliveryCommand;
import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.listeners.PlayerConnectionListeners;
import dev.siea.collections.managers.Manager;
import dev.siea.collections.messages.Messages;
import dev.siea.collections.papi.CollectionPlaceholder;
import dev.siea.collections.storage.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Collections extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        for (Player player : getServer().getOnlinePlayers()) {
            player.kickPlayer("§e[CollectionsX]: reloading scores...");
        }

        //Save config files
        saveDefaultConfig();
        saveResource("messages.yml", false);

        plugin = this;
        StorageManager.init(this);

        if (!isEnabled()) return;

        Messages.onEnable(this);
        Manager.enable(this);
        getServer().getPluginManager().registerEvents(new GUIWrapper(),this);
        Objects.requireNonNull(getCommand("collections")).setExecutor(new CollectionsCommand());
        Objects.requireNonNull(getCommand("delivery")).setExecutor(new DeliveryCommand());
        getServer().getPluginManager().registerEvents(new PlayerConnectionListeners(),this);
        getServer().getPluginManager().registerEvents(new CreationManager(),this);

        //Register Papi Placeholders
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CollectionPlaceholder(this).register();
            getLogger().info("Custom placeholders registered successfully.");
        } else {
            getLogger().warning("PlaceholderAPI not found. Custom placeholders will not work.");
        }
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
