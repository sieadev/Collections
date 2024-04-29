package dev.siea.collections.managers;

import dev.siea.collections.collections.*;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.storage.StorageManager;
import dev.siea.collections.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private static Plugin plugin;
    private static final HashMap<Integer, Collection> collections = new HashMap<>();

    public static Collection createCollection(Type type, String name, String description,  List<List<String>> rewards, Object target, boolean global, int level, int startingIndex){
        Collection collection;
        switch (type){
            case KILL:
                collection = new KillCollection(name, description, rewards, global, new Task(target,level,startingIndex));
                break;
            case BREAK:
                collection = new BreakCollection(name, description, rewards, global, new Task(target,level,startingIndex));
                break;
            case BREED:
                collection = new BreedCollection(name, description, rewards, global, new Task(target,level,startingIndex));
                break;
            case PLACE:
                collection = new PlaceCollection(name, description, rewards, global, new Task(target,level,startingIndex));
                break;
            case DELIVER:
                collection = new DeliverCollection(name, description, rewards, global, new Task(target,level,startingIndex));
                break;
            default:
                return null;
        }
        collections.put(collection.getID(), collection);
        plugin.getServer().getPluginManager().registerEvents((Listener) collection, plugin);
        GUIWrapper.addCollection(collection);
        return collection;
    }

    public static Collection createCollection(Type type, String name, String description, List<List<String>> rewards, Object target, boolean global, int level, int startingIndex, double multiplier){
        Collection collection;
        switch (type){
            case KILL:
                collection = new KillCollection(name, description,rewards,global, new Task(target,level,startingIndex,multiplier));
                break;
            case BREAK:
                collection = new BreakCollection(name, description,rewards,global, new Task(target,level,startingIndex,multiplier));
                break;
            case BREED:
                collection = new BreedCollection(name, description,rewards,global, new Task(target,level,startingIndex,multiplier));
                break;
            case PLACE:
                collection = new PlaceCollection(name, description,rewards,global, new Task(target,level,startingIndex,multiplier));
                break;
            case DELIVER:
                collection = new DeliverCollection(name, description,rewards,global, new Task(target,level,startingIndex,multiplier));
                break;
            default:
                return null;
        }
        collections.put(collection.getID(), collection);
        plugin.getServer().getPluginManager().registerEvents((Listener) collection, plugin);
        GUIWrapper.addCollection(collection);
        return collection;
    }

    public static Collection getCollection(String name){
        for (Collection collection : collections.values()){
            if (collection.getName().equals(name)){
                return collection;
            }
        }
        return null;
    }

    public static List<Collection> getCollections(){
        return (List<Collection>) collections.values();
    }

    public static void enable(Plugin plugin) {
        Manager.plugin = plugin;
        for (Collection collection : StorageManager.getCollections()){
            collections.put(collection.getID(), collection);
            plugin.getServer().getPluginManager().registerEvents((Listener) collection, plugin);
            GUIWrapper.addCollection(collection);
        }
    }

    public static HashMap<String, Integer> getPlayerScores(Player player) {
        HashMap<String, Integer> scores = new HashMap<>();
        for (Collection collection : collections.values()){
            int score = collection.getPlayerScore(player);
            if (score > -1) scores.put(String.valueOf(collection.getID()), score);
        }
        return scores;
    }

    public static void loadPlayerData(Player player) {
        HashMap<String, Integer> scores = StorageManager.getCollectionScores(player);
        for (Collection collection : collections.values()){
            for (String key : scores.keySet()){
                if (key.equals(String.valueOf(collection.getID()))){
                    collection.setPlayerScore(player, scores.get(key));
                }
            }
        }
    }

    public static void savePlayerData(Player player) {
        HashMap<String, Integer> scores = getPlayerScores(player);
        StorageManager.saveCollectionScores(player,scores);
    }

    public static void disable() {
        for (Player player : plugin.getServer().getOnlinePlayers()){
            savePlayerData(player);
        }
    }
}
