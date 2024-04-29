package dev.siea.collections.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface GUI {
    void handleInventoryClick(InventoryClickEvent e);
    Player getPlayer();
    Inventory getInventory();
}
