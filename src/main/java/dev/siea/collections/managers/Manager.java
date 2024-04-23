package dev.siea.collections.managers;

import dev.siea.collections.collections.*;
import dev.siea.collections.collections.task.Task;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manager {
    private static Plugin plugin;
    private static final List<Collection> collections = new ArrayList<>();

    public static void createCollection(Type type, String name, String description, Object target, boolean global, boolean inviteOnly, int level, int startingIndex){
        Collection collection;
        switch (type){
            case KILL:
                collection = new KillCollection(name, description, null, global, inviteOnly, new Task(target,level,startingIndex));
                break;
            case BREAK:
                collection = new BreakCollection(name, description, null, global, inviteOnly, new Task(target,level,startingIndex));
                break;
            case BREED:
                collection = new BreedCollection(name, description, null, global, inviteOnly, new Task(target,level,startingIndex));
                break;
            case PLACE:
                collection = new PlaceCollection(name, description, null, global, inviteOnly, new Task(target,level,startingIndex));
                break;
            case DELIVER:
                collection = new DeliverCollection(name, description, null, global, inviteOnly, new Task(target,level,startingIndex));
                break;
            default:
                return;
        }
        collections.add(collection);
        plugin.getServer().getPluginManager().registerEvents((Listener) collection, plugin);
    }

    public static void createCollection(Type type, String name, String description, Object target, boolean global, boolean inviteOnly, int level, int multiplier, int startingIndex){
        Collection collection;
        switch (type){
            case KILL:
                collection = new KillCollection(name, description,null,global, inviteOnly, new Task(target,level,startingIndex,multiplier));
                break;
            case BREAK:
                collection = new BreakCollection(name, description,null,global, inviteOnly, new Task(target,level,startingIndex,multiplier));
                break;
            case BREED:
                collection = new BreedCollection(name, description,null,global, inviteOnly, new Task(target,level,startingIndex,multiplier));
                break;
            case PLACE:
                collection = new PlaceCollection(name, description,null,global, inviteOnly, new Task(target,level,startingIndex,multiplier));
                break;
            case DELIVER:
                collection = new DeliverCollection(name, description,null,global, inviteOnly, new Task(target,level,startingIndex,multiplier));
                break;
            default:
                return;
        }
        collections.add(collection);
        plugin.getServer().getPluginManager().registerEvents((Listener) collection, plugin);
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
}
