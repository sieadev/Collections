package dev.siea.collections.gui;

import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.Type;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.siea.collections.util.GUIUtil.createItem;

public class CollectionsOverviewGUI implements GUI{
    private final HashMap<Integer, String> names = new HashMap<>();
    private final HashMap<Integer, Material> icons = new HashMap<>();
    private final HashMap<Integer, String> descriptions = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Type> types = new HashMap<>();


    public void addCollection(Collection collection) {
        icons.put(collection.getID(), Material.GRASS_BLOCK);
        descriptions.put(collection.getID(), collection.getDescription());
        tasks.put(collection.getID(), collection.getTasks());
        types.put(collection.getID(), collection.getType());
        names.put(collection.getID(), collection.getName());
    }

    public Inventory generateInventory(Player p) {
        Inventory inventory;
        HashMap<String, Integer> scores = Manager.getPlayerScores(p);
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
                description.add("§e§lTask:");
                description.add("§aType - §6" + types.get(keyInt).name());
                Object target = task.getTarget();
                if (target instanceof Material) {
                    description.add("§aBlock - §b" + target.toString().replace("_", " "));
                } else if (target instanceof EntityType){
                    description.add("§aTarget - §c" + target.toString().replace("_", " "));
                }
                description.add("§aScore - §6" + scores.get(key));
                ItemStack collection = createItem("§f" + names.get(keyInt), icons.get(keyInt), description);
                inventory.setItem(slot++, collection);
                description.add("");
                description.add("§eClick to view!");
            }
        }
        return inventory;
    }
}
