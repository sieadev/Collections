package dev.siea.collections.commands;

import dev.siea.collections.collections.Collection;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command cmd,@NotNull String label, String[] args) {
        if (sender instanceof Player) { GUIWrapper.openGUI((Player) sender, CollectionsOverviewGUI.class); }
        else {

            List<String> lvl1 = Collections.singletonList("msg siea You reached Level 1. Congrats!!");
            List<String> lvl2 = Collections.singletonList("msg siea You reached Level 2. Congrats!!");
            List<String> lvl3 = Collections.singletonList("msg siea You reached Level 3. Congrats!!");

            List<List<String>> rewards = new ArrayList<>();
            rewards.add(lvl1);
            rewards.add(lvl2);
            rewards.add(lvl3);

            Manager.createCollection(Type.KILL,"Zombie Slayer", "Slay Zombies to unlock what ever", rewards ,EntityType.ZOMBIE, true, 5, 3, 1.5);
        }
        return true;
    }
}
