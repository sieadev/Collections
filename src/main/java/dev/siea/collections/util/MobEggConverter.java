package dev.siea.collections.util;

import dev.siea.collections.Collections;
import org.bukkit.Material;
import org.bukkit.entity.Mob;

public class MobEggConverter {
    public static Material convertMobToEgg(Mob entityType) {
        return Collections.getPlugin().getServer().getItemFactory().getSpawnEgg(entityType.getType());
    }
}
