package dev.siea.collections.collections.common;

import org.bukkit.Material;

public enum Type {
    BREAK(Material.IRON_PICKAXE, "§eBreak"),
    PLACE(Material.COBBLESTONE, "§ePlace"),
    KILL(Material.IRON_SWORD, "§eKill"),
    DELIVER(Material.CHEST, "§eDeliver"),
    BREED(Material.WHEAT, "§eBreed");

    private final Material icon;
    private final String displayName;

    Type(Material icon, String displayName) {
        this.icon = icon;
        this.displayName = displayName;
    }

    public Material getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }
}
