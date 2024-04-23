package dev.siea.collections.commands;

import dev.siea.collections.collections.Type;
import dev.siea.collections.gui.CollectionsOverviewGUI;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.managers.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CollectionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command cmd,@NotNull String label, String[] args) {
        if (sender instanceof Player) { GUIWrapper.openGUI((Player) sender, CollectionsOverviewGUI.class); }
        else {
            Manager.createCollection(Type.KILL,"Zombie Slayer", "Slay Zombies to unlock what ever", EntityType.ZOMBIE, true, false, 5, 3, 1.5);
        }
        return true;
    }
}
