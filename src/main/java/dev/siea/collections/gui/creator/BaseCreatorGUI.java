package dev.siea.collections.gui.creator;

import dev.siea.collections.collections.Type;
import dev.siea.collections.creator.Creation;
import dev.siea.collections.creator.CreationManager;
import dev.siea.collections.creator.CreationState;
import dev.siea.collections.gui.GUI;
import dev.siea.collections.gui.GUIWrapper;
import dev.siea.collections.util.MobEggConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.siea.collections.util.GUIUtil.createItem;

public class BaseCreatorGUI implements GUI {
    private final Player player;
    private final Inventory inventory;
    private List<Integer> level = new ArrayList<Integer>();
    private String name;
    private String description;
    private Type type;
    private Object target;
    private boolean global = true;
    private List<List<String>> commands = new ArrayList<List<String>>();
    private boolean picked;
    private final HashMap<Integer, CreationState> buttons = new HashMap<>();

    public BaseCreatorGUI(Player player) {
        this.player = player;
        this.inventory = generateInventory();
    }

    public BaseCreatorGUI(Creation creation) {

        this.player = creation.getPlayer();

        this.name = creation.getName();
        this.description = creation.getDescription();
        this.type = creation.getType();
        this.target = creation.getTarget();
        this.global = creation.isGlobal();
        this.level = creation.getLevel();
        this.commands = creation.getCommands();

        this.inventory = generateInventory();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (buttons.containsKey(e.getSlot())) {
            picked = true;
            CreationState state = buttons.get(e.getSlot());
            CreationManager.initializeState(player,state);
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

    private Inventory generateInventory(){
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Collections Creator");

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack glass = createItem(" ", Material.GRAY_STAINED_GLASS_PANE);
            inventory.setItem(i, glass);
        }

        //Name
        ItemStack nameButton;
        if (name == null || name.isEmpty()) nameButton = createItem("§eName",CreationState.NAME.getIcon());
        else nameButton = createItem("§eName",CreationState.NAME.getIcon(), name);
        inventory.setItem(10, nameButton);
        buttons.put(10, CreationState.NAME);

        //Description
        ItemStack descriptionButton;
        if (description == null || description.isEmpty()) descriptionButton = createItem("§eDescription",CreationState.DESCRIPTION.getIcon());
        else descriptionButton = createItem("§eDescription",CreationState.DESCRIPTION.getIcon(), description);
        inventory.setItem(11, descriptionButton);
        buttons.put(11, CreationState.DESCRIPTION);

        //Type
        ItemStack typeButton;
        if (type == null) typeButton = createItem("§eType", CreationState.TYPE.getIcon());
        else {
            typeButton = createItem("§eType",type.getIcon());
            buttons.put(12, CreationState.TYPE);
        }
        inventory.setItem(12, typeButton);

        //Target
        ItemStack targetButton;
        if (target == null) targetButton = createItem("§eTarget", CreationState.TYPE.getIcon());
        else {
            if (target instanceof EntityType) {
                targetButton = createItem("§eTarget", MobEggConverter.convertMobToEgg((EntityType) target));
            }
            else {
                targetButton = createItem("§eTarget", (Material) target);
            }
        }
        if (type != null){
            buttons.put(13, CreationState.TARGET);
        }
        inventory.setItem(13, targetButton);

        //Global
        ItemStack globalButton;
        if (global){
            globalButton = createItem("§eGlobal", Material.GREEN_STAINED_GLASS);
        } else {
            globalButton = createItem("§eGlobal", Material.RED_STAINED_GLASS);
        }
        inventory.setItem(14, globalButton);
        buttons.put(14, CreationState.GLOBAL);

        //Level
        ItemStack levelButton;
        if (level.isEmpty()){
            levelButton = createItem("§eLevel", CreationState.LEVEL.getIcon());
        } else {
            List<String> description = new ArrayList<>();
            for (Integer i : level){
                description.add("- " + i);
            }
            levelButton = createItem("§eLevel", CreationState.LEVEL.getIcon(), description);
        }
        inventory.setItem(15, levelButton);
        buttons.put(15, CreationState.LEVEL);

        //Commands
        ItemStack commandsButton;
        if (commands.isEmpty()){
            commandsButton = createItem("§eCommands", CreationState.COMMANDS.getIcon());
        } else {
            List<String> description = new ArrayList<>();
            int level = 1;
            for (List<String> commandSection : commands){
                StringBuilder line = new StringBuilder("§7;");
                for (String command : commandSection){
                    line.append(String.valueOf("§7" + command));
                }
                description.add("§e§l" + level + " : §b" + line);
                level++;
            }
            commandsButton = createItem("§eCommands", CreationState.COMMANDS.getIcon(), description);
        }
        inventory.setItem(16, commandsButton);
        buttons.put(16, CreationState.COMMANDS);

        HashMap<CreationState, Boolean> options = new HashMap<>();


        //Required
        options.put(CreationState.NAME, (name != null && !name.isEmpty()));
        options.put(CreationState.DESCRIPTION, (description != null && !description.isEmpty()));
        options.put(CreationState.TYPE, (type != null));
        options.put(CreationState.TARGET, (target != null));

        int missing = 0;
        for (CreationState state : options.keySet()){
            if (!options.get(state))missing++;
        }

        //Not required
        options.put(CreationState.GLOBAL, false);
        options.put(CreationState.COMMANDS, commands.isEmpty());
        options.put(CreationState.LEVEL, level.isEmpty());

        List<String> description = new ArrayList<>();
        if (missing > 0) {
            description.add("§c§lMissing " + missing + " required options");
        }
        else {
            description.add("§a§lMissing 0 required options");
        }
        for (CreationState state : options.keySet()){
            if (options.get(state)){
                description.add("§a" + state.getDisplayName() + " - §a§lSET");
            }
            else {
                description.add("§c" + state.getDisplayName() + " - §c§lMISSING");
            }
        }

        //Finish
        ItemStack finishButton;
        if (missing < 1){
            finishButton = createItem("§aFinish", Material.GREEN_TERRACOTTA , description);
            buttons.put(26, CreationState.FINISH);
        } else {
            finishButton = createItem("§cFinish", Material.BARRIER, description);
        }
        inventory.setItem(26, finishButton);
        return inventory;
    }
}
