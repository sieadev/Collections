package dev.siea.collections.collections;

import dev.siea.collections.collections.other.Task;
import dev.siea.collections.storage.StorageManager;
import dev.siea.collections.util.LevelUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.List;

public class KillCollection implements Collection, Listener {
    private final String name;
    private final String description;
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final boolean global;
    private final Task tasks;
    private final EntityType entityType;
    private final List<Integer> level;
    private final List<List<String>> commands;
    private final int id;

    public KillCollection(String name, String description, List<List<String>> commands, boolean global, Task task) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        entityType = (EntityType) task.getTarget();

        this.id = StorageManager.registerCollection(this);
    }

    public KillCollection(String name, String description, List<List<String>> commands, boolean global, Task task, int id) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        entityType = (EntityType) task.getTarget();

        this.id = id;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if (e.getEntity().getType() != entityType) return;
        Player player = e.getEntity().getKiller();
        if (player == null) return;
        if (!global && !scores.containsKey(player)) return;
        int oldScore = scores.getOrDefault(player, 0);
        int newScore = oldScore + 1;
        scores.put(player, newScore);
        LevelUtil.checkLevel(player, oldScore, newScore, level, this);
    }

    @Override
    public int getID() {
        return 0;
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
        return Type.KILL;
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
    public List<List<String>> getCommands() {
        return commands;
    }

    @Override
    public int getPlayerScore(Player player){
        return scores.getOrDefault(player, -1);
    }
}

