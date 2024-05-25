package dev.siea.collections.gui.deliver;

import dev.siea.collections.collections.common.Task;
import dev.siea.collections.collections.deliver.DeliverCollection;
import dev.siea.collections.gui.GUI;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import static dev.siea.collections.gui.GUIWrapper.*;
import static dev.siea.collections.gui.GUIWrapper.names;
import static dev.siea.collections.util.GUIUtil.createItem;

public class DeliverGUI implements GUI{
    private final Player player;
    private final Inventory inventory;
    private final DeliverCollection manager;

    public DeliverGUI(Player player, int id, int score, DeliverCollection manager) {
        this.manager = manager;
        this.player = player;
        Inventory inventory;
        Task task = tasks.get(id);


        inventory = Bukkit.createInventory(null, 5 * 9, names.get(id));


        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack glass = createItem(" ", Material.GRAY_STAINED_GLASS_PANE);
            inventory.setItem(i, glass);
        }


        List<String> description = new ArrayList<>();
        int keyInt = Integer.parseInt(String.valueOf(id));
        description.add("§7" + descriptions.get(keyInt));
        int nextLevel = LevelUtil.getNextLevel(task, score);
        int currentLevel = LevelUtil.getCurrentLevel(task, score);
        int requiredScore = LevelUtil.getScoreToNextLevel(task, score, nextLevel);

        StringBuilder name = new StringBuilder().append("§f§e").append(names.get(keyInt));

        if (nextLevel > 0) {
            name.append(" ").append(RomanConverter.toRoman(currentLevel));
            description.add("");
            int percent = (int) LevelUtil.getPercentToLevel(score, requiredScore);
            if (percent < 0) { percent = 0; }
            description.add("§7Progress to " + names.get(keyInt) + " "+ RomanConverter.toRoman(nextLevel) + ": §e" + percent + "§6%");
            description.add(LevelUtil.generateBar(score, requiredScore) + "§r §e(" + score + "/" + requiredScore + ")");
        }
        else{
            name.append(" ").append("§aCOMPLETE");
        }
        ItemStack collection = createItem(name.toString(), icons.get(keyInt), description);
        inventory.setItem(4, collection);

        List<String> deliverDescription = new ArrayList<>();
        int amount = InventoryUtil.countMaterial(player,(Material) task.getTarget());
        if (amount > 0) {
            deliverDescription.add("§eClick to deliver §6" + amount + " §e" + StringUtils.capitalize(((Material) task.getTarget()).name().replace("_", " ").toLowerCase()));
        } else {
            deliverDescription.add("§cYou dont have any §6" + StringUtils.capitalize(((Material) task.getTarget()).name().replace("_", " ").toLowerCase()));
        }

        ItemStack deliver = createItem("§6§lDeliver", Material.CHEST, deliverDescription);
        inventory.setItem(4+9+9, deliver);

        ItemStack close = createItem("§cClose", Material.BARRIER);
        inventory.setItem(inventory.getSize()-5, close);

        this.inventory = inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getSlot();
        if (slot == inventory.getSize()-5) {
            GUIWrapper.close(inventory);
            e.getWhoClicked().closeInventory();
        }
        else if (slot == 4+9+9){
            manager.deliver(player);
            GUIWrapper.close(inventory);
            e.getWhoClicked().closeInventory();
        };
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent e) {
        GUIWrapper.close(inventory);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
