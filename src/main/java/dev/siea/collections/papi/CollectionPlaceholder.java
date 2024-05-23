package dev.siea.collections.papi;

import dev.siea.collections.api.CollectionsAPI;
import dev.siea.collections.collections.common.Collection;
import dev.siea.collections.collections.common.Task;
import dev.siea.collections.util.LevelUtil;
import dev.siea.collections.util.RomanConverter;
import dev.siea.collections.util.StringUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class CollectionPlaceholder extends PlaceholderExpansion {

    private final String identifier;
    private final String version;
    private final String author;

    public CollectionPlaceholder(Plugin plugin) {
        identifier = "Collection";
        version = plugin.getDescription().getVersion();
        author = plugin.getDescription().getName();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String param) {
        try{
            ArrayList<String> args = new ArrayList<>(Arrays.asList(param.split("_")));
            Collection collection = CollectionsAPI.getCollection(Integer.parseInt(args.get(0)));

            int score = collection.getPlayerScore((Player) player);
            int nextLevel = LevelUtil.getNextLevel(collection.getTasks(),collection.getPlayerScore((Player) player));
            Task task = collection.getTasks();
            int scoreToNextLevel = LevelUtil.getScoreToNextLevel(task,score,nextLevel);

            return switch (args.get(1)) {
                //General Data
                case "description" -> collection.getDescription();
                case "type" -> collection.getType().getDisplayName();
                case "target" -> StringUtils.capitalize(collection.getTasks().getTarget().toString().replace("_", " "));
                case "levelSize" -> String.valueOf(collection.getTasks().getLevel().size());
                //Player Specific Data
                case "level" -> RomanConverter.toRoman(LevelUtil.getCurrentLevel(task,score));
                case "nextLevel" -> RomanConverter.toRoman(nextLevel);
                case "score" -> String.valueOf(score);
                case "scoreToNext" -> String.valueOf(scoreToNextLevel);
                case "bar" -> LevelUtil.generateBar(score, scoreToNextLevel);
                default -> collection.getName();
            };
        } catch (Exception e){
            return "§cNo Data";
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return version;
    }

    @Override
    public @NotNull String getVersion() {
        return author;
    }
}
