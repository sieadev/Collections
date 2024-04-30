package dev.siea.collections.util;

import dev.siea.collections.Collections;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class MobEggConverter {
    public static Material convertMobToEgg(EntityType entityType) {
        return Collections.getPlugin().getServer().getItemFactory().getSpawnEgg(entityType);
    }
}
