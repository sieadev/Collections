package dev.siea.collections.util;

import dev.siea.collections.Collections;
import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.other.Task;
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
            player.sendMessage(Messages.get("levelUp").replace("%newLevel%", String.valueOf(currentLevel)).replace("%collection%", collection.getName()).replace("%previousLevel%", String.valueOf(currentLevel - 1)));
        }
    }

    public static int getNextLevel(Task task, int score) {
        int nextLevel = -1;
        List<Integer> level = task.getLevel();
        for (int i = 0; i < level.size(); i++) {
            if (score < level.get(i)) {
                nextLevel = level.get(i);
                break;
            }
        }
        return nextLevel;
    }

    public static int getScoreToNextLevel(Task task, int score) {
        int nextLevelScore = getNextLevel(task, score);
        if (nextLevelScore == -1) {
            return 0;
        } else {
            return nextLevelScore - score;
        }
    }

    public static double getPercentToLevel(Task task, int currentScore, int targetLevel) {
        List<Integer> levels = task.getLevel();
        if (targetLevel <= 0 || targetLevel > levels.size()) {
            throw new IllegalArgumentException("Invalid target level: " + targetLevel);
        }

        int targetScore = levels.get(targetLevel - 1);
        if (currentScore >= targetScore) {
            return 100.0; // Already achieved or surpassed the target level
        } else {
            int previousLevelScore = (targetLevel > 1) ? levels.get(targetLevel - 2) : 0;
            int scoreNeeded = targetScore - previousLevelScore;
            int scoreProgress = currentScore - previousLevelScore;
            return (double) scoreProgress / scoreNeeded * 100.0;
        }
    }
}
