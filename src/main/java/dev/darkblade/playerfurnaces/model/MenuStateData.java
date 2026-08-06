package dev.darkblade.playerfurnaces.model;

import org.bukkit.Material;
import java.util.List;

public class MenuStateData {
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final Integer customModelData;
    private final String skullOwner;
    private final String skullTexture;

    public MenuStateData(Material material, String name, List<String> lore) {
        this(material, name, lore, null, null, null);
    }

    public MenuStateData(Material material, String name, List<String> lore, Integer customModelData, String skullOwner, String skullTexture) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
        this.skullOwner = skullOwner;
        this.skullTexture = skullTexture;
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

    public Integer getCustomModelData() {
        return customModelData;
    }

    public String getSkullOwner() {
        return skullOwner;
    }

    public String getSkullTexture() {
        return skullTexture;
    }
}
