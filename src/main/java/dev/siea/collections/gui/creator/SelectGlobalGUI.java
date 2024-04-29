package dev.siea.collections.gui.creator;

import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static dev.siea.collections.util.GUIUtil.createItem;

public class SelectGlobalGUI implements GUI {
    private final Player player;
    private final Inventory inventory;

    public SelectGlobalGUI(Player player) {
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
        CreationManager.handleInventoryClickEvent(e);
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
