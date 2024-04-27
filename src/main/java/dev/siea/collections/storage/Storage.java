package dev.siea.collections.storage;

import dev.siea.collections.collections.Collection;
import java.util.List;

public interface Storage {
    public List<Collection> getCollections();

    int saveCollection(Collection collection);
}
