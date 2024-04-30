package dev.siea.collections.gui;

import dev.siea.collections.collections.Collection;
import dev.siea.collections.collections.Type;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.creator.Creation;
import dev.siea.collections.gui.creator.BaseCreatorGUI;
import dev.siea.collections.gui.creator.SelectGlobalGUI;
import dev.siea.collections.gui.creator.SelectMobGUI;
import dev.siea.collections.gui.creator.SelectTypeGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GUIWrapper implements Listener {
    private static final HashMap<Inventory, GUI> inventories = new HashMap<>();
    public static final HashMap<Integer, String> names = new HashMap<>();
    public static final HashMap<Integer, Material> icons = new HashMap<>();
    public static final HashMap<Integer, String> descriptions = new HashMap<>();
    public static final HashMap<Integer, Task> tasks = new HashMap<>();
    public static final HashMap<Integer, Type> types = new HashMap<>();

    public GUIWrapper() {

    }

    public static void openGUI(Player player, Class<? extends GUI> clazz) {
        GUI gui;
        if (clazz.equals(CollectionsOverviewGUI.class)) {
            gui = new CollectionsOverviewGUI(player);
        } else if (clazz.equals(CollectionsCreatorGUI.class)) {
            gui = new CollectionsCreatorGUI(player);
        } else if (clazz.equals(SelectTypeGUI.class)) {
            gui = new SelectTypeGUI(player);
        } else if (clazz.equals(SelectGlobalGUI.class)) {
            gui = new SelectGlobalGUI(player);
        } else if (clazz.equals(BaseCreatorGUI.class)) {
            gui = new BaseCreatorGUI(player);
        } else if (clazz.equals(SelectMobGUI.class)) {
            gui = new SelectMobGUI(player);
        } else {
            gui = null;
        }
        if (gui == null) {
            player.sendMessage("§cUnable to open GUI-Inventory...");
            return;
        }
        inventories.put(gui.getInventory(), gui);
        player.openInventory(gui.getInventory());
    }

    public static void openGUI(Player player, Creation creation) {
        GUI gui = new BaseCreatorGUI(creation);
        inventories.put( gui.getInventory(), gui);
        player.openInventory(gui.getInventory());
    }

    public static void addCollection(Collection collection) {
        switch (collection.getType()) {
            case DELIVER -> icons.put(collection.getID(), Material.CHEST);
            case BREED -> icons.put(collection.getID(), Material.WHEAT);
            case KILL -> icons.put(collection.getID(), Material.IRON_SWORD);
            case PLACE -> icons.put(collection.getID(), Material.COBBLESTONE);
            case BREAK -> icons.put(collection.getID(), Material.IRON_PICKAXE);
        }
        descriptions.put(collection.getID(), collection.getDescription());
        tasks.put(collection.getID(), collection.getTasks());
        types.put(collection.getID(), collection.getType());
        names.put(collection.getID(), collection.getName());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!inventories.containsKey(e.getInventory())) return;
        GUI gui = inventories.get(e.getInventory());
        if (gui != null) {
            gui.handleInventoryClick(e);
        }
        else{
            e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent e) {
        GUI gui = inventories.get(e.getInventory());
        if (gui != null) {
            gui.handleInventoryClose(e);
        }
    }

    public static void close(Inventory inventory){
        inventories.remove(inventory);
    }
}
