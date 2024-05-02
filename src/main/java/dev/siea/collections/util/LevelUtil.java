package dev.siea.collections.util;

import dev.siea.collections.Collections;
import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.messages.Messages;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

        if (currentLevel == level.size()) {
            player.sendMessage(Messages.get("complete").replace("%newLevel%", String.valueOf(currentLevel)).replace("%collection%", collection.getName()).replace("%previousLevel%", String.valueOf(currentLevel - 1)));
        }
        else if (currentLevel != -1) {
            try{
                List<List<String>> commands = collection.getCommands();
                List<String> command = commands.get(currentLevel-1);
                for (String cmd : command){
                    Collections.getPlugin().getServer().dispatchCommand(Collections.getPlugin().getServer().getConsoleSender(), cmd);
                }
            } catch (Exception ignore){
            }
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(Messages.get("levelUp").replace("%newLevel%", String.valueOf(currentLevel)).replace("%collection%", collection.getName()).replace("%previousLevel%", String.valueOf(currentLevel - 1)));
        }
    }

    public static int getNextLevel(Task task, int score) {
        int currentLevel = getCurrentLevel(task,score);
        if (task.getLevel().size() > currentLevel) return currentLevel+1;
        else return -1;
    }

    public static int getScoreToNextLevel(Task task, int score, int level) {
        int nextLevel = getNextLevel(task, score);
        if (nextLevel == -1) {
            return 0;
        } else {
            return task.getLevel().get(nextLevel-1);
        }
    }

    public static double getPercentToLevel(double currentScore, double requiredScore) {
        return (int) ((currentScore / requiredScore) * 100);
    }

    public static int getCurrentLevel(Task task, int currentScore) {
        List<Integer> levels = task.getLevel();
        int currentLevel = 0;
        for (int i = 0; i < levels.size(); i++) {
            if (currentScore >= levels.get(i)) {
                currentLevel = i + 1;
            } else {
                break;
            }
        }
        return currentLevel;
    }

    public static String generateBar(int currentScore, int requiredScore) {
        int totalLength = 35;
        return getBar(currentScore, requiredScore, totalLength);
    }


    public static String generateBar(int currentScore, int requiredScore, int length) {
        return getBar(currentScore, requiredScore, length);
    }

    @NotNull
    private static String getBar(double currentScore, int requiredScore, int totalLength) {
        double percentFilled = currentScore / requiredScore;
        int filledLength = (int) (totalLength * percentFilled);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < totalLength; i++) {
            if (i <= filledLength) {
                bar.append("§a§m ");
            } else {
                bar.append("§e§m ");
            }
        }
        return bar.toString();
    }
}
