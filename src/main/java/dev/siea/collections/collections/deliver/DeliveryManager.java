package dev.siea.collections.collections.deliver;

import dev.siea.collections.gui.GUIWrapper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;
import java.util.Objects;

public class DeliveryManager {
    private final int id;
    private final DeliverCollection collection;
    private final NamespacedKey key = new NamespacedKey(dev.siea.collections.Collections.getPlugin(), "collection_id");

    public DeliveryManager(int id, DeliverCollection collection) {
        this.id = id;
        this.collection = collection;
    }

    public boolean isStation(Block block) {
        return Objects.equals(getCustomID(block), String.valueOf(id));
    }

    public void openDeliveryGUI(Player player) {
        GUIWrapper.openGUI(player, id, collection.getPlayerScore(player),collection);
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        if (getCustomID(event.getItemInHand()) == null && Objects.equals(getCustomID(event.getItemInHand()), String.valueOf(id))) {
            placeStation(event.getBlock());
        }
    }

    private void placeStation(Block block){
        block.setType(Material.CHEST);
        TileState state = (TileState) block.getState();
        state.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.valueOf(id));
        state.update();
    }

    public ItemStack deliverStationItem(){
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(collection.getName());
            meta.setLore(Collections.singletonList("§7Place this block anywhere to setup a delivery station for this collection"));
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(key, PersistentDataType.STRING, String.valueOf(id));
            item.setItemMeta(meta);
        }
        return item;
    }

    private String getCustomID(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return "non";
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return "non";
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        return dataContainer.get(key, PersistentDataType.STRING);
    }

    public String getCustomID(Block block) {
        if (block.getState() instanceof TileState state) {
            if (state.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                return state.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            }
        }
        return "non";
    }
}
