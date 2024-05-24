package dev.siea.collections.collections.deliver;

import dev.siea.collections.collections.common.Collection;
import dev.siea.collections.collections.common.Type;
import dev.siea.collections.collections.common.Task;
import dev.siea.collections.messages.Messages;
import dev.siea.collections.storage.StorageManager;
import dev.siea.collections.util.LevelUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class DeliverCollection implements Collection, Listener {

    private final String name;
    private final String description;
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final boolean global;
    private final Task tasks;
    private final Material block;
    private final List<Integer> level;
    private final List<List<String>> commands;
    private final int id;
    private final DeliveryManager deliveryManager;

    public DeliverCollection(String name, String description, List<List<String>> commands, boolean global, Task task) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        block = (Material) task.getTarget();

        this.id = StorageManager.registerCollection(this);
        this.deliveryManager = new DeliveryManager(id,this);
    }

    public DeliverCollection(String name, String description, List<List<String>> commands, boolean global, Task task, int id) {
        this.name = name;
        this.description = description;
        this.global = global;
        this.tasks = task;
        this.level = task.getLevel();
        this.commands = commands;
        block = (Material) task.getTarget();
        this.id = id;
        this.deliveryManager = new DeliveryManager(id,this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Block block = e.getClickedBlock();
        if (block != null && deliveryManager.isStation(block)){
            deliveryManager.openDeliveryGUI(e.getPlayer());
        }
    }

    public void openGUI(Player player){
        deliveryManager.openDeliveryGUI(player);
    }

    @EventHandler
    public void onPlayerInteract(BlockPlaceEvent e){
        deliveryManager.onBlockPlace(e);
    }

    public void deliver(Player player) {
        if (!global && !scores.containsKey(player)) return;

        int totalItemsRemoved = removeItemsFromInventory(player, block);

        if(totalItemsRemoved <= 0){
            String message = Messages.get("nothingToDeliver");
            player.sendMessage(message);
            return;
        } else {
            String message = Messages.get("delivered").replace("%totalItemsDelivered%", String.valueOf(totalItemsRemoved));
            player.sendMessage(message);
        }

        int oldScore = scores.getOrDefault(player, 0);
        int newScore = oldScore + totalItemsRemoved;
        scores.put(player, newScore);
        LevelUtil.checkLevel(player, oldScore, newScore, level, this);
    }

    private int removeItemsFromInventory(Player player, Material material) {
        Inventory inventory = player.getInventory();
        int totalCount = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item != null && item.getType() == material) {
                totalCount += item.getAmount();
                inventory.setItem(i, null);
            }
        }
        return totalCount;
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
        return Type.DELIVER;
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
