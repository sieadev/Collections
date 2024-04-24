package dev.siea.collections.collections;

import dev.siea.collections.collections.other.Task;
import dev.siea.collections.util.LevelUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.List;

public class PlaceCollection implements Collection, Listener {

    private final String name;
    private final String description;
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final boolean global;
    private final Task tasks;
    private final Material block;
    private final List<Integer> level;
    private final List<List<String>> commands;

    public PlaceCollection(String name, String description, List<List<String>> commands, boolean global, Task task) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        block = (Material) task.getTarget();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getBlock().getType() != block) return;
        Player player = e.getPlayer();
        if (!global && !scores.containsKey(player)) return;
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
        return Type.BREAK;
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
