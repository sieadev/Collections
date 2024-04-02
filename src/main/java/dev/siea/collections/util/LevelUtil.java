package dev.siea.collections.util;

import dev.siea.collections.Collections;
import org.bukkit.entity.Player;

import java.util.List;

public class LevelUtil {
    public static void checkLevel(Player player, int oldScore, int newScore, List<Integer> level, List<List<String>> commands) {
        int currentLevel = -1;
        for (int i = 0; i < level.size(); i++) {
            if (newScore >= level.get(i) && oldScore < level.get(i)) {
                currentLevel = i + 1;
                break;
            }
        }

        if (currentLevel != -1) {
            System.out.println(player.getName() + " leveled up to tier " + currentLevel);
            try{
                Collections.getPlugin().getServer().dispatchCommand(Collections.getPlugin(),);
            } catch (Exception e){

            }
        }
    }
}
