package dev.siea.collections.creator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;

public class CreationManager implements Listener {
    private final static HashMap<Player, Creation> creations = new HashMap<>();

    public static void enterCreator(Player player) {
        creations.put(player, new Creation(player));
    }

    public static void leaveCreator(Player player) {
        try {
            creations.get(player).finishOrLeave();
            creations.remove(player);
        } catch (Exception ignore){
        }
    }

    public static void initializeState(Player player, CreationState state) {
        creations.get(player).initializeState(state);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onAsyncPlayerChat(PlayerChatEvent event) {
        Creation creation = creations.get(event.getPlayer());
        if (creation != null) {
            event.setCancelled(true);
            creation.handleChatMessage(event.getMessage());
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerEnteredCommand(PlayerCommandPreprocessEvent event){
        Creation creation = creations.get(event.getPlayer());
        if (creation != null) {
            event.setCancelled(true);
            creation.handleChatMessage(event.getMessage());
        }
    }

    public static void handleInventoryClickEvent(Player player, CreationState state, Object object) {
        Creation creation = creations.get(player);
        if (creation != null) {
            creation.handleInventoryClick(state, object);
        }
    }
}
