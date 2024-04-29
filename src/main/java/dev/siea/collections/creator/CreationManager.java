package dev.siea.collections.creator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class CreationManager implements Listener {
    private final static HashMap<Player, Creation> creations = new HashMap<>();

    public static void enterCreator(Player player) {
        creations.put(player, new Creation(player));
    }

    public static void leaveCreator(Player player) {
        Creation creation = creations.get(player);
        creation.cancel();
        creations.remove(player);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        creations.get(event.getPlayer()).handleChatMessage(event.getMessage());
    }

    public static void handleInventoryClickEvent(InventoryClickEvent event, Object object) {
        creations.get((Player) event.getWhoClicked()).handleInventoryClick(event, object);
    }
}
