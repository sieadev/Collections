package dev.siea.collections.commands;

import dev.siea.collections.gui.CollectionsCreatorGUI;
import dev.siea.collections.gui.CollectionsOverviewGUI;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.StringJoiner;

public class CollectionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command cmd,@NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "help" -> {
                        player.sendMessage(generateHelpMessage(player));
                    }
                    case "modify", "edit" , "create", "admin" -> {
                        if (player.hasPermission("collections.create")) {
                            GUIWrapper.openGUI(player, CollectionsCreatorGUI.class);
                        }
                        else{
                            GUIWrapper.openGUI(player, CollectionsOverviewGUI.class);
                        }
                    }
                    default -> GUIWrapper.openGUI(player, CollectionsOverviewGUI.class);
                }
            }
            else{
                GUIWrapper.openGUI((Player) sender, CollectionsOverviewGUI.class);
            }
        }
        else {
            sender.sendMessage(Messages.get("notAPlayer"));
        }
        return true;
    }

    private String generateHelpMessage(Player player) {
        HashMap<String, String> commands = new HashMap<>();
        if (player.hasPermission("collections.overview")) {
            commands.put("/collections", Messages.get("overviewCommand"));
        }
        if (player.hasPermission("collections.create")) {
            commands.put("/collections create", Messages.get("createCommand"));
        }

        StringJoiner complete = new StringJoiner("\n");
        complete.add("§n§l§eCollection Commands:");
        for (String command : commands.keySet()) {
            complete.add("§e" + command + "§7 - " + commands.get(command));
        }
        return complete.toString();
    }
}
