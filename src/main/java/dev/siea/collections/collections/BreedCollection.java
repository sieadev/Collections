package dev.siea.collections.collections;

import dev.siea.collections.collections.common.Collection;
import dev.siea.collections.collections.common.Task;
import dev.siea.collections.collections.common.Type;
import dev.siea.collections.storage.StorageManager;
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
    private final Task tasks;
    private final EntityType entityType;
    private final List<Integer> level;
    private final List<List<String>> commands;
    private final int id;

    public BreedCollection(String name, String description, List<List<String>> commands, boolean global, Task task) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        entityType = (EntityType) task.getTarget();

        this.id = StorageManager.registerCollection(this);
    }

    public BreedCollection(String name, String description, List<List<String>> commands, boolean global, Task task, int id) {
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
    public void onEntityBread(EntityBreedEvent e){
        if (e.getEntity().getType() != entityType) return;
        Player player = (Player) e.getBreeder();
        if (!global && !scores.containsKey(player)) return;
        int oldScore = scores.getOrDefault(player, 0);
        int newScore = oldScore + 1;
        scores.put(player, newScore);
        LevelUtil.checkLevel(player, oldScore, newScore, level, this);
    }

    @Override
    public int getID() {
        return id;
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
    public List<List<String>> getCommands() {
        return commands;
    }

    @Override
    public int getPlayerScore(Player player){
        return scores.getOrDefault(player, -1);
    }

    @Override
    public void setPlayerScore(Player player, int score) {
        scores.put(player,score);
    }
}
