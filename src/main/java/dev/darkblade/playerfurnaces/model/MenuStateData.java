package dev.darkblade.playerfurnaces.model;

import org.bukkit.Material;
import java.util.List;

public class MenuStateData {
    private final Material material;
    private final String name;
    private final List<String> lore;

    public MenuStateData(Material material, String name, List<String> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }
}
