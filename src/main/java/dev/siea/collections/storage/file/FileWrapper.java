package dev.siea.collections.storage.file;

import dev.siea.collections.collections.Collection;
import dev.siea.collections.storage.Storage;

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
}
