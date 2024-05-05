package dev.siea.collections.api;

import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.managers.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionsAPI {

    public static Map<Integer, Integer> getCollectionScores(Player player) {
        Map<String, Integer> scores = Manager.getPlayerScores(player);
        Map<Integer, Integer> scoresAsID = new HashMap<>();
        for (String entry : scores.keySet()) {
            scoresAsID.put(Integer.parseInt(entry), scores.get(entry));
        }
        return scoresAsID;
    }

    public static Collection getCollection(int id) {
        return Manager.getCollection(id);
    }

    public static Map<Integer, Task> getTasks() {
        List<Collection> collections = Manager.getCollections();
        Map<Integer, Task> tasks = new HashMap<>();
        for (Collection collection : collections) {
            tasks.put(collection.getID(), collection.getTasks());
        }
        return tasks;
    }

    public static Task getTask(int id) {
        Collection collection = Manager.getCollection(id);
        assert collection != null;
        return collection.getTasks();
    }
}
