package dev.siea.collections.gui;

import dev.siea.collections.collections.other.Task;
import dev.siea.collections.creator.CreationManager;
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
import java.util.List;

import static dev.siea.collections.gui.GUIWrapper.*;
import static dev.siea.collections.util.GUIUtil.createItem;

public class CollectionSelectedGUI  implements GUI{
    private final Player player;
    private final Inventory inventory;

    public CollectionSelectedGUI(Player player, int id, int score) {
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
        Object target = task.getTarget();
        int nextLevel = LevelUtil.getNextLevel(task, score);
        int currentLevel = LevelUtil.getCurrentLevel(task, score);
        int requiredScore = LevelUtil.getScoreToNextLevel(task, score, nextLevel);

        StringBuilder name = new StringBuilder().append("§f§e").append(names.get(keyInt));

        if (nextLevel > 0) {
            name.append(" ").append(RomanConverter.toRoman(currentLevel));
            description.add("");
            int percent = (int) LevelUtil.getPercentToLevel(score, requiredScore);
            description.add("§7Progress to " + StringUtils.capitalize(target.toString().replace("_", " ")) + " "+ RomanConverter.toRoman(nextLevel) + ": §e" + percent + "§6%");
            description.add(LevelUtil.generateBar(score, requiredScore));
        }
        else{
            name.append(" ").append("§aCOMPLETE");
        }
        ItemStack collection = createItem(name.toString(), icons.get(keyInt), description);
        inventory.setItem(4, collection);

        int slot = 22 - (task.getLevel().size() / 2);
        int levelCounter = 1;
        for (int scoreForLevel : task.getLevel()){
            ItemStack level;

            List<String> desc = new ArrayList<>();
            desc.add("");
            int percent = (int) LevelUtil.getPercentToLevel(score, scoreForLevel);
            desc.add("§7Progress to " + names.get(keyInt) + " "+ RomanConverter.toRoman(levelCounter) + ": §e" + percent + "§6%");
            desc.add(LevelUtil.generateBar(score, scoreForLevel));
            if (score >= scoreForLevel){
                level = createItem(names.get(keyInt) + " "+ RomanConverter.toRoman(levelCounter), Material.GREEN_STAINED_GLASS_PANE, desc);
            } else{
                level = createItem(names.get(keyInt) + " "+ RomanConverter.toRoman(levelCounter), Material.RED_STAINED_GLASS_PANE, desc);
            }
            inventory.setItem(slot, level);
            slot++;
            levelCounter++;
        }

        if (slot % 2 == 0){
            ItemStack filler = createItem(" ", Material.BARRIER);
            inventory.setItem(slot, filler);
        }


        ItemStack back = createItem("§cBack", Material.ARROW);
        inventory.setItem(inventory.getSize()-6, back);

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
        else if (slot == inventory.getSize()-6) {
            GUIWrapper.close(inventory);
            GUIWrapper.openGUI(player, CollectionsOverviewGUI.class);
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
