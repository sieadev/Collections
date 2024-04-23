package dev.siea.collections.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;
import java.util.List;

public class GUIUtil {

    /**
     * Create a GUI button with a String lore
     *
     * @param name Name as String
     * @param material Icon as material
     * @param lore Lore as String
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material, String lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Create a GUI button with a String-List lore
     *
     * @param name Name as String
     * @param material Icon as material
     * @param lore Lore as String-List
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Create a GUI button
     *
     * @param name Name as String
     * @param material Icon as material
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
