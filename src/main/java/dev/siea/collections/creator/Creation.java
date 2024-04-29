package dev.siea.collections.creator;

import dev.siea.collections.collections.Type;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.gui.creator.SelectGlobalGUI;
import dev.siea.collections.gui.creator.SelectTypeGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class Creation {
    private final Player player;
    private String name;
    private String description;
    private Type type;
    private Object target;
    private List<Integer> level;
    private List<List<String>> commands;
    private boolean global;
    private CreationState state;
    private int commandStage = 1;

    public Creation(Player player) {
        this.player = player;
        state = CreationState.NAME;
        messageInstructions();
    }

    private void next(){
        state = upEnum(state);
        messageInstructions();
    }

    private void finish() {

    }

    private void messageInstructions(){
        String message;
        switch (state){
            case NAME -> {
                message = "§eChoose a name for your collection §7(formatting codes supported) :";
            }
            case DESCRIPTION -> {
                message = "§ePerfect! Choose a description for §b"+ name + " §e:";
            }
            case TYPE -> {
                GUIWrapper.openGUI(player, SelectTypeGUI.class);
                message = "";
            }
            case TARGET -> {
                if (type == Type.BREAK || type == Type.PLACE || type == Type.DELIVER){
                    message = "§eAlright!Now, hold the target block and type: §6SELECT";
                } else{
                    message = "§eAlright!Now, hold the egg of the target-mob and type: §6SELECT";
                }
            }
            case LEVEL -> {
                message = "§eSplendid, now type all level as their required score §7(eg. 10 30 50 100)";
            }
            case COMMANDS -> {
                message = "§eAlmost there, enter all commands without the §b/ §e for level §b " + commandStage + " §e(and then type §6DONE§e)";
            }
            case GLOBAL -> {
                GUIWrapper.openGUI(player, SelectGlobalGUI.class);
                message = "";
            }
            default -> message = "";
        }
        player.sendMessage(message);
    }


    public void cancel() {
        player.sendMessage("§cYou left the collection creator");
    }

    public void handleChatMessage(String message) {

    }

    public void handleInventoryClick(InventoryClickEvent event) {

    }

    public CreationState upEnum(CreationState currentStatus) {
        CreationState[] statuses = CreationState.values();
        int currentIndex = currentStatus.ordinal();
        int nextIndex = (currentIndex + 1) % statuses.length;
        return statuses[nextIndex];
    }

    public CreationState downEnum(CreationState currentStatus) {
        CreationState[] statuses = CreationState.values();
        int currentIndex = currentStatus.ordinal();
        int previousIndex = (currentIndex - 1 + statuses.length) % statuses.length;
        return statuses[previousIndex];
    }
}
