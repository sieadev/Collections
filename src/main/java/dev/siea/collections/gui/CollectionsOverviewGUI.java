package dev.siea.collections.gui;

import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.Type;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.creator.CreationState;
import dev.siea.collections.managers.Manager;
import dev.siea.collections.util.LevelUtil;
import dev.siea.collections.util.RomanConverter;
import dev.siea.collections.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.siea.collections.util.GUIUtil.createItem;
import static dev.siea.collections.gui.GUIWrapper.tasks;
import static dev.siea.collections.gui.GUIWrapper.icons;
import static dev.siea.collections.gui.GUIWrapper.names;
import static dev.siea.collections.gui.GUIWrapper.descriptions;

public class CollectionsOverviewGUI implements GUI{
    private final Player player;
    private final Inventory inventory;
    private final HashMap<Integer, Integer> buttons = new HashMap<>();
    private final HashMap<String, Integer> scores;

    public CollectionsOverviewGUI(Player p) {
        this.player = p;
        Inventory inventory;
        scores = Manager.getPlayerScores(p);
        if (scores.size() < 8) {
            inventory = Bukkit.createInventory(null, 3 * 9, "Collections");
        }
        else {
            inventory = Bukkit.createInventory(null, 5 * 9, "Collections");
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack glass = createItem(" ", Material.GRAY_STAINED_GLASS_PANE);
            inventory.setItem(i, glass);
        }

        if (scores.isEmpty()){
            ItemStack empty = createItem("§cNothing here it seems...", Material.BARRIER);
            inventory.setItem(13, empty);
        }

        else{
            int slot = 10;
            for (String key : scores.keySet()) {
                if (slot == 17 || slot == 26|| slot == 35) {
                    slot = slot + 2;
                }
                List<String> description = new ArrayList<>();
                int keyInt = Integer.parseInt(key);
                Task task = tasks.get(keyInt);
                description.add("§7" + descriptions.get(keyInt));
                Object target = task.getTarget();
                int currentScore = scores.get(key);
                int nextLevel = LevelUtil.getNextLevel(task, currentScore);
                int currentLevel = LevelUtil.getCurrentLevel(task, currentScore);
                int requiredScore = LevelUtil.getScoreToNextLevel(task, currentScore, nextLevel);

                StringBuilder name = new StringBuilder().append("§f§e").append(names.get(keyInt));

                if (nextLevel > 0) {
                    name.append(" ").append(RomanConverter.toRoman(currentLevel));
                    description.add("");
                    int percent = (int) LevelUtil.getPercentToLevel(currentScore, requiredScore);
                    description.add("§7Progress to " + name + " "+ RomanConverter.toRoman(nextLevel) + ": §e" + percent + "§6%");
                    description.add(LevelUtil.generateBar(currentScore, requiredScore));
                }
                else{
                    name.append(" ").append("§aCOMPLETE");
                }

                description.add("");
                description.add("§eClick to view!");
                ItemStack collection = createItem(name.toString(), icons.get(keyInt), description);
                inventory.setItem(slot, collection);
                buttons.put(slot,keyInt);
                slot++;
            }

            ItemStack close = createItem("§cClose", Material.BARRIER);
            inventory.setItem(inventory.getSize()-5, close);
        }
        this.inventory = inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (buttons.containsKey(e.getSlot())) {
            int id = buttons.get(e.getSlot());
            GUIWrapper.close(inventory);
            GUIWrapper.openGUI(player,id, scores.get(String.valueOf(id)));
        }
        else if (e.getSlot() == inventory.getSize()-5) {
            GUIWrapper.close(inventory);
            e.getWhoClicked().closeInventory();
        }
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
