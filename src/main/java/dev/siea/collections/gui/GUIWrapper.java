package dev.siea.collections.gui;

import dev.siea.collections.collections.Collection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GUIWrapper implements Listener {
    private static final HashMap<Inventory, Player> inventories = new HashMap<>();
    private static final CollectionsOverviewGUI overviewGUI = new CollectionsOverviewGUI();

    public GUIWrapper() {

    }

    public static void openGUI(Player player, Class<? extends GUI> clazz) {
        Inventory inventory;
        if (clazz.equals(CollectionsOverviewGUI.class)) {
            inventory = overviewGUI.generateInventory(player);
        } else {
            inventory = null;
        }
        if (inventory == null) {
            player.sendMessage("§cUnable to open GUI-Inventory...");
            return;
        }
        inventories.put(inventory, player);
        player.openInventory(inventory);
    }

    public static void addCollection(Collection collection){
        overviewGUI.addCollection(collection);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!inventories.containsKey(e.getInventory())) return;
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        inventories.remove(e.getInventory());
    }
}
