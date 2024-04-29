package dev.siea.collections.gui.creator;

import dev.siea.collections.collections.Type;
import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import static dev.siea.collections.util.GUIUtil.createItem;

public class SelectTypeGUI implements GUI {
    private final Player player;
    private final Inventory inventory;

    public SelectTypeGUI(Player player) {
        this.player = player;
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Collections Creator");


        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack glass = createItem(" ", Material.GRAY_STAINED_GLASS_PANE);
            inventory.setItem(i, glass);
        }

        int slot = 10;

        for (int i = 0; i < 7; i++) {
            if (slot == 17 || slot == 26|| slot == 35) {
                slot = slot + 2;
            }
            ItemStack blank = createItem("§cComing Soon!", Material.BARRIER);
            inventory.setItem(slot++, blank);
        }

        slot = 10;

        for (Type type : Type.values()) {
            if (slot == 17 || slot == 26|| slot == 35) {
                slot = slot + 2;
            }
            ItemStack collection = createItem("§f" + type.getDisplayName(), type.getIcon());
            inventory.setItem(slot++, collection);
        }

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
