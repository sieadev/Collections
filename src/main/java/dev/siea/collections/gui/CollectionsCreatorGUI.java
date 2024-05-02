package dev.siea.collections.gui;

import dev.siea.collections.collections.other.Task;
import dev.siea.collections.creator.CreationManager;
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

import static dev.siea.collections.gui.GUIWrapper.tasks;
import static dev.siea.collections.gui.GUIWrapper.icons;
import static dev.siea.collections.gui.GUIWrapper.names;
import static dev.siea.collections.gui.GUIWrapper.types;
import static dev.siea.collections.gui.GUIWrapper.descriptions;
import static dev.siea.collections.util.GUIUtil.createItem;

public class CollectionsCreatorGUI implements GUI{
    private final Player player;
    private final Inventory inventory;

    public CollectionsCreatorGUI(Player player) {
        this.player = player;
        Inventory inventory;
        List<Integer> ids = tasks.keySet().stream().toList();

        if (ids.size() < 8) {
            inventory = Bukkit.createInventory(null, 4 * 9, "Collections Creator");
        }
        else {
            inventory = Bukkit.createInventory(null, 6 * 9, "Collections Creator");
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack glass = createItem(" ", Material.GRAY_STAINED_GLASS_PANE);
            inventory.setItem(i, glass);
        }

        if (ids.isEmpty()){
            ItemStack empty = createItem("§cNothing here it seems...", Material.BARRIER);
            inventory.setItem(13, empty);
        }

        else{
            int slot = 10;
            for (Integer key : ids) {
                if (slot == 17 || slot == 26|| slot == 35) {
                    slot = slot + 2;
                }
                List<String> description = new ArrayList<>();
                Task task = tasks.get(key);
                description.add("§7" + descriptions.get(key));
                description.add("§e§lTask:");
                description.add("§aType - §6" + types.get(key).name());
                Object target = task.getTarget();
                if (target instanceof Material) {
                    description.add("§aBlock - §b" + target.toString().replace("_", " "));
                } else if (target instanceof EntityType){
                    description.add("§aTarget - §c" + target.toString().replace("_", " "));
                }
                description.add("");
                description.add("§e§mClick to view!§r §c§lComing Soon!");
                ItemStack collection = createItem("§f" + names.get(key), icons.get(key), description);
                inventory.setItem(slot++, collection);
            }
            ItemStack create = createItem("§6Creator" , Material.EMERALD_BLOCK, "§eClick to open the Creator");
            inventory.setItem(inventory.getSize()-5, create);
        }
        this.inventory = inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getSlot();
        if (slot == inventory.getSize()-5){
            GUIWrapper.close(inventory);
            player.closeInventory();
            CreationManager.enterCreator(player);
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
