package dev.siea.collections.collections;

import dev.siea.collections.collections.other.Task;
import org.bukkit.entity.Player;

import java.util.List;

public interface Collection {
    String getName();
    String getDescription();
    Type getType();
    List<Player> getParticipants();
    int getPlayerScore(Player player);
    Task getTasks();
    boolean isGlobal();
    List<List<String>> getCommands();
}
