package dev.siea.collections.util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
public class InventoryUtil {
    public static int countMaterial(Player player, Material material) {
        Inventory inventory = player.getOpenInventory().getBottomInventory();
        int totalCount = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == material) {
                totalCount += item.getAmount();
            }
        }
        return totalCount;
    }
}
