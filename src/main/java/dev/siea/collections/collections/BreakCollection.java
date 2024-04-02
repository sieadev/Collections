package dev.siea.collections.collections;

import dev.siea.collections.collections.task.Task;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.List;

public class BreakCollection implements Collection, Listener {

    private final String name;
    private final String description;
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final boolean global;
    private final boolean inviteOnly;
    private final Task tasks;
    private final Material block;
    private final List<Integer> level;
    public BreakCollection(String name, String description, boolean global, boolean inviteOnly, Task task) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.inviteOnly = inviteOnly;
        this.tasks = task;
        this.level = task.getLevel();
        block = (Material) task.getTarget();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (e.getBlock().getType() != block) return;
        Player player = e.getPlayer();
        if (inviteOnly && !scores.containsKey(player)) return;
        int oldScore = scores.getOrDefault(player, 0);
        int newScore = scores.getOrDefault(player, 0)+1;
        scores.put(player, newScore);
        checkLevel(player, oldScore, newScore);
    }

    private void checkLevel(Player player, int oldScore, int newScore) {
        int currentLevel = -1;
        for (int i = 0; i < level.size(); i++) {
            if (newScore >= level.get(i) && oldScore < level.get(i)) {
                currentLevel = i + 1;
                break;
            }
        }

        if (currentLevel != -1) {
            //Do what ever
            System.out.println(player.getName() + " leveled up to tier " + currentLevel);
        }
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
    public boolean requiresInvite() {
        return inviteOnly;
    }
}
