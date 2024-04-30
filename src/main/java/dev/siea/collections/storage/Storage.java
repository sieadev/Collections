package dev.siea.collections.storage;

import dev.siea.collections.collections.Collection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface Storage {
    List<Collection> getCollections();

    int saveCollection(Collection collection);

    HashMap<String, Integer> getCollectionScores(Player player);

    void saveCollectionScores(Player player, HashMap<String, Integer> scores);

    void shutdown();
}
