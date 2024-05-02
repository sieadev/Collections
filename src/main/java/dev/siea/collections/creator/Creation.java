package dev.siea.collections.creator;

import dev.siea.collections.collections.Type;
import dev.siea.collections.collections.other.Task;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.gui.creator.BaseCreatorGUI;
import dev.siea.collections.gui.creator.SelectGlobalGUI;
import dev.siea.collections.gui.creator.SelectMobGUI;
import dev.siea.collections.gui.creator.SelectTypeGUI;
import dev.siea.collections.managers.Manager;
import dev.siea.collections.messages.Messages;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Creation {
    private final Player player;
    private String name;
    private String description;
    private Type type = null;
    private Object target = null;
    private List<Integer> level = new ArrayList<>();
    private final List<List<String>> commands = new ArrayList<>();
    private boolean global = true;
    private CreationState state;
    private int commandStage = 1;
    private boolean finished = false;

    public Creation(Player player) {
        this.player = player;
        GUIWrapper.openGUI(player, BaseCreatorGUI.class);
    }

    private void openCreatorGUI(){
        state = null;
        GUIWrapper.openGUI(player, this);
    }

    private void finish() {
        player.closeInventory();
        if (level.isEmpty()) level = generateLevels(5,3,2);
        finished = true;
        Manager.createCollection(type,name,description,commands,global,new Task(target,level));
        player.sendMessage("§eSuccessfully created §b" + name + "§e collection.");
        CreationManager.leaveCreator(player);
    }

    public void initializeState(CreationState state) {
        this.state = state;
        switch (state) {
            case NAME -> {
                player.sendMessage("§eEnter the name into the chat §b(formating-codes supported)§e:");
                player.closeInventory();
            }
            case DESCRIPTION -> {
                player.sendMessage("§eEnter the description into the chat §b(formating-codes supported)§e:");
                player.closeInventory();
            }
            case TYPE -> GUIWrapper.openGUI(player, SelectTypeGUI.class);
            case TARGET -> {
                if (type == null){
                    openCreatorGUI();
                }
                else if (type == Type.KILL || type == Type.BREED){
                    GUIWrapper.openGUI(player, SelectMobGUI.class);
                }
                else{
                    player.sendMessage("§eHold the target item of the collection and type §6SELECT§e.");
                    player.closeInventory();
                }
            }
            case GLOBAL -> GUIWrapper.openGUI(player, SelectGlobalGUI.class);
            case LEVEL -> {
                player.sendMessage("§eEnter all level you want to add identified by their require score §b(eg. 5 15 45)§e:");
                player.closeInventory();
            }
            case COMMANDS -> {
                if (level.isEmpty()){
                    openCreatorGUI();
                    return;
                }
                player.sendMessage("§eEnter a command you would like executed on level §b" + commandStage + "§e:");
                player.closeInventory();
            }
            case FINISH -> finish();
        }
    }


    public void cancel() {
        player.sendMessage(Messages.get("leftCreation"));
    }

    public void handleChatMessage(String message) {
        message = message.replace("&","§");
        if (message.equalsIgnoreCase("cancel")){
            player.sendMessage("§cCancelling...");
            openCreatorGUI();
            return;
        }

        switch (state){
            case NAME -> {
                message = "§7" + message;
                name = message;
                openCreatorGUI();
            }
            case DESCRIPTION -> {
                message = "§7" + message;
                description = message;
                openCreatorGUI();
            }
            case TYPE -> {
                return;
            }
            case TARGET -> {
                if (!message.equalsIgnoreCase("select")){
                    player.sendMessage("§cUnknown input. Did you mean §6SELECT§c?");
                    return;
                }
                if (type == Type.BREAK || type == Type.PLACE || type == Type.DELIVER){
                    if (player.getInventory().getItemInMainHand().getType() != Material.AIR){
                        target = player.getInventory().getItemInMainHand().getType();
                        openCreatorGUI();
                    }
                    else{
                        player.sendMessage("§cPlease hold an item in your main hand.");
                    }
                } else{
                    return;
                }
            }
            case LEVEL -> {
                List<Integer> level = new ArrayList<>();
                String[] words =message.split("\\s");
                for (String word : words){
                    try{
                        int l = Integer.parseInt(word);
                        level.add(l);
                    } catch (NumberFormatException e){
                        player.sendMessage("§cUnable to process levels. §c(eg. §65 15 45§c)");
                        return;
                    }
                }
                this.level = level;
                openCreatorGUI();
            }
            case COMMANDS -> {
                commands.add(Collections.singletonList(message.replace("/" , "")));
                if (commandStage >= level.size()){
                    openCreatorGUI();
                }
                else{
                    commandStage++;
                    player.sendMessage("§eEnter a command you would like executed on level §b" + commandStage + "§e:");
                }
            }
            case GLOBAL -> {
                break;
            }
            case FINISH -> finish();
        }
    }

    public void handleInventoryClick(CreationState state, Object object) {
        switch (state){
            case TYPE -> {
                type = (Type) object;
                openCreatorGUI();
            }
            case GLOBAL -> {
                global = (boolean) object;
                openCreatorGUI();
            }
            case TARGET -> {
                target = object;
                openCreatorGUI();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Object getTarget() {
        return target;
    }

    public List<Integer> getLevel() {
        return level;
    }

    public List<List<String>> getCommands() {
        return commands;
    }

    public boolean isGlobal() {
        return global;
    }

    public CreationState getState() {
        return state;
    }

    public int getCommandStage() {
        return commandStage;
    }

    private List<Integer> generateLevels(int startingIndex, int levelAmount, double levelMultiplier) {
        List<Integer> levels = new ArrayList<>();
        double currentValue = startingIndex;
        for (int i = 0; i < levelAmount; i++) {
            levels.add((int) currentValue);
            currentValue *= levelMultiplier;
        }
        return levels;
    }

    public void finishOrLeave() {
        if (!finished) {
            cancel();
        }
    }
}
