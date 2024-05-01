package dev.siea.collections.gui.creator;

import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.creator.CreationState;
import dev.siea.collections.gui.GUI;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.util.MobEggConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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
    private final HashMap<Integer, EntityType> mobs = new HashMap<>();

    public SelectMobGUI(Player player) {
        this.player = player;
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Select a mob");

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack glass = createItem(" ", Material.GRAY_STAINED_GLASS_PANE);
            inventory.setItem(i, glass);
        }

        int slot = 0;

        for (EntityType m : EntityType.values()) {
            try{
                ItemStack glass = createItem("§e" + m.name(), MobEggConverter.convertMobToEgg(m));
                inventory.setItem(slot, glass);
                mobs.put(slot, m);
                slot++;
            }  catch (Exception e) {
                continue;
            }
        }

        this.inventory = inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (mobs.containsKey(e.getSlot())) {
            picked = true;
            CreationManager.handleInventoryClickEvent(player, CreationState.TARGET, mobs.get(e.getSlot()));
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
