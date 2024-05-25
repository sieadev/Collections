package dev.siea.collections.commands;
import dev.siea.collections.collections.deliver.DeliverCollection;
import dev.siea.collections.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DeliveryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command cmd,@NotNull String label, String[] args) {
        if (args.length > 2) { return false; }

        DeliverCollection collection;
        try {
            collection = (DeliverCollection) Manager.getCollection(Integer.parseInt(args[0]));
            if (collection == null) { throw new Exception("Collection not found or not a delivery Collection"); }
        } catch (Exception e) {
            sender.sendMessage("Collection not found or not a delivery Collection");
            return true;
        }
        String playerSomething = args[1];
        Player player = null;
        try{
            player = Bukkit.getPlayer(playerSomething); 
        } catch (Exception ignore){
            try {
                player = Bukkit.getPlayer(UUID.fromString(playerSomething));
            } catch (Exception ignore2){
            }
        }
        
        if (player == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        collection.openGUI(player);
        return true;
    }
}
