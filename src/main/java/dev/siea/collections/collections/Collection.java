package dev.siea.collections.collections;

import dev.siea.collections.collections.task.Task;
import org.bukkit.entity.Player;

import java.util.List;

public interface Collection {
    String getName();
    String getDescription();
    Type getType();
    List<Player> getParticipants();
    Task getTasks();
    boolean isGlobal();
    boolean requiresInvite();
}
