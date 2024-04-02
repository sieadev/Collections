package dev.siea.collections.managers;

import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.KillCollection;
import dev.siea.collections.collections.Type;
import dev.siea.collections.collections.task.Task;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manager {
    private static Plugin plugin;
    private static final List<Collection> collections = new ArrayList<>();
    public Manager(Plugin plugin){
        Manager.plugin = plugin;
    }

    public static void createCollection(Type type, String name, String description, Objects target, boolean global, boolean inviteOnly, int level, int startingIndex){

        Collection collection = null;

        switch (type){
            case KILL:
                collection = new KillCollection(name, description, global, inviteOnly, new Task(target,level,startingIndex));
            case BREAK:
            case BREED:
            case PLACE:
            case PICKUP:
            case DELIVER:
        }
        collections.add(collection);
    }

    public static void createCollection(Type type, String name, String description, Objects target, boolean global, boolean inviteOnly, int level, int multiplier, int startingIndex){

        Collection collection = null;

        switch (type){
            case KILL:
                collection = new KillCollection(name, description, global, inviteOnly, new Task(target,level,startingIndex,multiplier));
            case BREAK:
            case BREED:
            case PLACE:
            case PICKUP:
            case DELIVER:
        }
        collections.add(collection);
    }
}
