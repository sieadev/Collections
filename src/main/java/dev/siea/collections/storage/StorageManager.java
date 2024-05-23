package dev.siea.collections.storage;

import dev.siea.collections.collections.common.Collection;
import dev.siea.collections.storage.file.FileWrapper;
import dev.siea.collections.storage.mysql.MySQLWrapper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StorageManager {
    private static Storage storage;


    public static void init(Plugin plugin) {
        try {
            String storageType = plugin.getConfig().getString("storage");
            String ip = plugin.getConfig().getString("database.ip");
            String name = plugin.getConfig().getString("database.name");
            String url = "jdbc:mysql://" + ip + "/" + name;
            String user = plugin.getConfig().getString("database.user");
            String psw = plugin.getConfig().getString("database.password");
            switch (Objects.requireNonNull(storageType)) {
                case "MYSQL":
                    storage = new MySQLWrapper(url,user,psw);
                    break;
                case "FILE":
                    storage = new FileWrapper();
                default:
                    throw new Exception("Unsupported storage type: " + storageType);
            }
        } catch (Exception e) {
            if (plugin.getConfig().getBoolean("fileAsFallback")) {
                plugin.getLogger().severe("Switching to File Storage (fileAsFallback) due to invalid Storage Type or connection failure!");
                storage = new FileWrapper();
            } else {
                plugin.getLogger().severe(String.format("[%s] - Disabled due to invalid Storage Type or connection failure! [THIS IS NOT A BUG DO NOT REPORT IT]", plugin.getDescription().getName()));
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

    public static Storage getStorage() {
        return storage;
    }

    public static int registerCollection(Collection collection){
        return storage.saveCollection(collection);
    }

    public static List<Collection> getCollections(){
        return storage.getCollections();
    }

    public static HashMap<String, Integer> getCollectionScores(Player player) {
        return storage.getCollectionScores(player);
    }

    public static void saveCollectionScores(Player player, HashMap<String, Integer> scores) {
        storage.saveCollectionScores(player, scores);
    }

    public static void shutdown(){
        storage.shutdown();
    }
}
