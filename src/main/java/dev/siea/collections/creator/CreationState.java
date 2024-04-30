package dev.siea.collections.creator;

import org.bukkit.Material;

public enum CreationState {
    NAME(Material.NAME_TAG, "§eName"),
    DESCRIPTION(Material.NAME_TAG, "§eDescription"),
    TYPE(Material.BARREL, "§eType"),
    TARGET(Material.TARGET, "§eTarget"),
    LEVEL(Material.EXPERIENCE_BOTTLE, "§eLevel"),
    COMMANDS(Material.COMMAND_BLOCK, "§eCommands"),
    GLOBAL(Material.PLAYER_HEAD, "§eGlobal"),
    FINISH(Material.GREEN_STAINED_GLASS,"§eFinish");

    private final Material icon;
    private final String displayName;

    CreationState(Material icon, String displayName) {
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
