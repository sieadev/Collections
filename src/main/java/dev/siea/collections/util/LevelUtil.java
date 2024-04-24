package dev.siea.collections.util;

import dev.siea.collections.Collections;
import dev.siea.collections.collections.Collection;
import dev.siea.collections.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LevelUtil {
    public static void checkLevel(Player player, int oldScore, int newScore, List<Integer> level, Collection collection) {

        if (newScore == 1) {
            player.sendMessage(Messages.get("unlocked").replace("%collection%", collection.getName()));
        }
        
        int currentLevel = -1;
        for (int i = 0; i < level.size(); i++) {
            if (newScore >= level.get(i) && oldScore < level.get(i)) {
                currentLevel = i + 1;
                break;
            }
        }

        if (currentLevel != -1) {
            try{
                List<List<String>> commands = collection.getCommands();
                List<String> command = commands.get(currentLevel-1);
                for (String cmd : command){
                    Collections.getPlugin().getServer().dispatchCommand(Collections.getPlugin().getServer().getConsoleSender(), cmd);
                }
            } catch (Exception e){
                System.out.println("Unable to execute command for " + player.getName());
            }
            player.sendMessage(Messages.get("levelUp").replace("%level%", String.valueOf(currentLevel)).replace("%collection%", collection.getName()));
        }
    }
}
