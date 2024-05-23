package dev.siea.collections.storage.file;

import dev.siea.collections.collections.common.Collection;
import dev.siea.collections.storage.Storage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class FileWrapper implements Storage {
    @Override
    public List<Collection> getCollections() {
        return null;
    }

    @Override
    public int saveCollection(Collection collection) {
        return 0;
    }

    @Override
    public HashMap<String, Integer> getCollectionScores(Player player) {
        return null;
    }

    @Override
    public void saveCollectionScores(Player player, HashMap<String, Integer> scores) {

    }

    @Override
    public void shutdown() {

    }
}
