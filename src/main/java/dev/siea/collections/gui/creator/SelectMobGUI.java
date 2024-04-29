package dev.siea.collections.gui.creator;

import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.gui.GUI;
import dev.siea.collections.gui.GUIWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static dev.siea.collections.util.GUIUtil.createItem;

public class SelectMobGUI implements GUI {
    private final Player player;
    private final Inventory inventory;
    private boolean picked;
    private final HashMap<Integer, Mob> mobs = new HashMap<>();

    public SelectMobGUI(Player player) {
        this.player = player;
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Collections Creator");
        ItemStack global = createItem("§bGlobal", Material.BLUE_STAINED_GLASS);
        inventory.setItem(12, global);
        ItemStack invite = createItem("§eInvite only", Material.YELLOW_STAINED_GLASS);
        inventory.setItem(14, invite);
        this.inventory = inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (mobs.containsKey(e.getSlot())) {
            CreationManager.handleInventoryClickEvent(e, mobs.get(e.getSlot()));
            picked = true;
            e.getWhoClicked().closeInventory();
        }
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent e) {
        if (!picked){
            e.getPlayer().openInventory(inventory);
        }
        else{
            GUIWrapper.close(inventory);
        }
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
