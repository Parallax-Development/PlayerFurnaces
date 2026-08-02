package dev.darkblade.playerfurnaces.manager;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.model.MenuLayout;
import dev.darkblade.playerfurnaces.model.MenuSlotData;
import dev.darkblade.playerfurnaces.model.MenuStateData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuManager {

    private final PlayerFurnacesPlugin plugin;
    private File menusFile;
    private FileConfiguration menusConfig;
    private final Map<String, MenuLayout> layouts;

    public MenuManager(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
        this.layouts = new HashMap<>();
        loadMenus();
    }

    public void loadMenus() {
        this.menusFile = new File(plugin.getDataFolder(), "menus.yml");
        if (!menusFile.exists()) {
            plugin.saveResource("menus.yml", false);
        }
        this.menusConfig = YamlConfiguration.loadConfiguration(menusFile);
        this.layouts.clear();

        for (String key : menusConfig.getKeys(false)) {
            parseLayout(key);
        }
    }

    private void parseLayout(String menuKey) {
        ConfigurationSection section = menusConfig.getConfigurationSection(menuKey);
        if (section == null) return;

        String title = colorize(section.getString("title", "Menu"));
        List<String> layoutStrings = section.getStringList("layout");
        int size = layoutStrings.size() * 9;
        
        MenuLayout layout = new MenuLayout(title, size);
        ConfigurationSection legend = section.getConfigurationSection("legend");
        
        if (legend == null) {
            layouts.put(menuKey, layout);
            return;
        }

        // Parse legend first
        Map<Character, MenuSlotData> legendMap = new HashMap<>();
        for (String charKey : legend.getKeys(false)) {
            if (charKey.length() != 1 && !charKey.equals("#")) continue;
            char c = charKey.charAt(0);
            
            ConfigurationSection slotSection = legend.getConfigurationSection(charKey);
            if (slotSection == null) continue;
            
            String type = slotSection.getString("type", "FILLER");
            
            MenuStateData defaultState = parseState(slotSection);
            
            Map<String, MenuStateData> states = new HashMap<>();
            ConfigurationSection statesSection = slotSection.getConfigurationSection("states");
            if (statesSection != null) {
                for (String stateKey : statesSection.getKeys(false)) {
                    ConfigurationSection stateDataSection = statesSection.getConfigurationSection(stateKey);
                    if (stateDataSection != null) {
                        states.put(stateKey, parseState(stateDataSection));
                    }
                }
            } else {
                // Parse inline active/waiting/inactive states if they exist
                for (String inlineStatePrefix : new String[]{"active", "waiting", "inactive"}) {
                    if (slotSection.contains(inlineStatePrefix + "_material") || slotSection.contains(inlineStatePrefix + "_name")) {
                        Material mat = Material.matchMaterial(slotSection.getString(inlineStatePrefix + "_material", defaultState.getMaterial() != null ? defaultState.getMaterial().name() : "STONE"));
                        String name = colorize(slotSection.getString(inlineStatePrefix + "_name", defaultState.getName()));
                        List<String> lore = slotSection.getStringList(inlineStatePrefix + "_lore").stream().map(this::colorize).collect(Collectors.toList());
                        if (lore.isEmpty()) lore = defaultState.getLore();
                        
                        states.put(inlineStatePrefix, new MenuStateData(mat, name, lore));
                    }
                }
            }
            
            legendMap.put(c, new MenuSlotData(type, defaultState, states));
        }

        // Apply layout to slots
        int slotIndex = 0;
        int dynamicIndex = 1;
        
        for (String row : layoutStrings) {
            for (char c : row.toCharArray()) {
                if (c == ' ') {
                    slotIndex++;
                    continue; // Skip empty space
                }
                
                MenuSlotData slotData = legendMap.get(c);
                if (slotData != null) {
                    layout.getSlots().put(slotIndex, slotData);
                    
                    if ("FURNACE_SLOT".equals(slotData.getType()) || c == '#') {
                        layout.getDynamicSlotMap().put(dynamicIndex, slotIndex);
                        dynamicIndex++;
                    }
                }
                slotIndex++;
            }
        }
        
        layouts.put(menuKey, layout);
    }

    private MenuStateData parseState(ConfigurationSection section) {
        Material material = Material.STONE;
        String matStr = section.getString("material");
        if (matStr != null) {
            Material matched = Material.matchMaterial(matStr);
            if (matched != null) material = matched;
        }
        
        String name = colorize(section.getString("name", ""));
        List<String> lore = section.getStringList("lore").stream().map(this::colorize).collect(Collectors.toList());
        
        return new MenuStateData(material, name, lore);
    }

    public MenuLayout getLayout(String menuName) {
        return layouts.get(menuName);
    }

    private String colorize(String text) {
        if (text == null) return null;
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
