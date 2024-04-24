package dev.siea.collections.managers;

import dev.siea.collections.collections.*;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.gui.GUIWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private static Plugin plugin;
    private static final List<Collection> collections = new ArrayList<>();

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
        collections.add(collection);
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
        collections.add(collection);
        plugin.getServer().getPluginManager().registerEvents((Listener) collection, plugin);
        GUIWrapper.addCollection(collection);
        return collection;
    }

    public static Collection getCollection(String name){
        for (Collection collection : collections){
            if (collection.getName().equals(name)){
                return collection;
            }
        }
        return null;
    }

    public static void enable(Plugin plugin) {
        Manager.plugin = plugin;
    }

    public static void shutdown(){
        //Save collections to storage
    }

    public static HashMap<String, Integer> getPlayerScores(Player player) {
        HashMap<String, Integer> scores = new HashMap<>();
        for (Collection collection : collections){
            int score = collection.getPlayerScore(player);
            if (score > -1) scores.put(collection.getName(), score);
        }
        return scores;
    }
}
