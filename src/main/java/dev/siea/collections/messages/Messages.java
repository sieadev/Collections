package dev.siea.collections.messages;

import dev.siea.collections.Collections;
import dev.siea.collections.util.ConfigUtil;
import dev.siea.collections.util.RomanConverter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Messages {
    private static final HashMap<String, String> messages = new HashMap<>();
    private static Plugin plugin;
    private static final ConfigurationSection configUtil = new ConfigUtil(Collections.getPlugin(), "messages.yml").getConfig();

    public static void onEnable(Plugin plugin){
        Messages.plugin = plugin;
        RomanConverter.init(configUtil.getBoolean("useRomanNumerals"));
    }
    public static String get(String key) {
        return messages.computeIfAbsent(key, Messages::retrieveMessageFromConfig);
    }

    private static String retrieveMessageFromConfig(String key) {
        String retrievedMessage = configUtil.getString(key);
        if (retrievedMessage == null) {
            retrievedMessage = "§c§lThis is not a bug do not report it! §c[Collections >> messages.yml] The following message is either missing or not set: §e" + key;
        }
        return retrievedMessage.replace("&","§");
    }

    public static void reload() {
        messages.clear();
    }
}




