package dev.siea.collections.collections;

import dev.siea.collections.collections.task.Task;
import dev.siea.collections.util.LevelUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.HashMap;
import java.util.List;

public class BreedCollection implements Collection, Listener {

    private final String name;
    private final String description;
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final boolean global;
    private final boolean inviteOnly;
    private final Task tasks;
    private final EntityType entityType;
    private final List<Integer> level;
    private final List<List<String>> commands;

    public BreedCollection(String name, String description, List<List<String>> commands, boolean global, boolean inviteOnly, Task task) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.inviteOnly = inviteOnly;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        entityType = (EntityType) task.getTarget();
    }

    @EventHandler
    public void onEntityBread(EntityBreedEvent e){
        if (e.getEntity().getType() != entityType) return;
        Player player = (Player) e.getBreeder();
        if (inviteOnly && !scores.containsKey(player)) return;
        int oldScore = scores.getOrDefault(player, 0);
        int newScore = oldScore + 1;
        scores.put(player, newScore);
        LevelUtil.checkLevel(player, oldScore, newScore, level, this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Type getType() {
        return Type.BREED;
    }

    @Override
    public List<Player> getParticipants() {
        return (List<Player>) scores.keySet();
    }

    @Override
    public Task getTasks() {
        return tasks;
    }

    @Override
    public boolean isGlobal() {
        return global;
    }

    @Override
    public boolean requiresInvite() {
        return inviteOnly;
    }

    @Override
    public List<List<String>> getCommands() {
        return commands;
    }

    @Override
    public int getPlayerScore(Player player){
        return scores.getOrDefault(player, -1);
    }
}
