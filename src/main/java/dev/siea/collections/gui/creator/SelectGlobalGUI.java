package dev.siea.collections.gui.creator;

import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.creator.CreationState;
import dev.siea.collections.gui.GUI;
import dev.siea.collections.gui.GUIWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static dev.siea.collections.util.GUIUtil.createItem;

public class SelectGlobalGUI implements GUI {
    private final Player player;
    private final Inventory inventory;
    private boolean picked;

    public SelectGlobalGUI(Player player) {
        this.player = player;
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Global or Invite");
        ItemStack global = createItem("§bGlobal", Material.GREEN_STAINED_GLASS);
        inventory.setItem(12, global);
        ItemStack invite = createItem("§eInvite only", Material.RED_STAINED_GLASS);
        inventory.setItem(14, invite);
        this.inventory = inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getSlot() == 12 || e.getSlot() == 14) {
            picked = true;
            if (e.getSlot() == 12){
                CreationManager.handleInventoryClickEvent(player, CreationState.GLOBAL, true);
            }
            else{
                CreationManager.handleInventoryClickEvent(player, CreationState.GLOBAL, false);
            }
        }
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent e) {
        if (!picked){
            CreationManager.leaveCreator(player);
            GUIWrapper.close(inventory);
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
