package dev.siea.collections.collections;

import dev.siea.collections.collections.task.Task;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class PlaceCollection implements Collection{

    private final String name;
    private final String description;
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final boolean global;
    private final boolean inviteOnly;
    private final Task tasks;
    public PlaceCollection(String name, String description, boolean global, boolean inviteOnly, Task tasks) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.inviteOnly = inviteOnly;
        this.tasks = tasks;
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
        return Type.PLACE;
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