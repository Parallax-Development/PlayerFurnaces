package dev.darkblade.playerfurnaces.model;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class RecipeItemDefinition {

    private final String id;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final Integer customModelData;
    private final Map<String, String> pdc;
    private final int amount;

    public RecipeItemDefinition(String id, Material material, String name, List<String> lore, Integer customModelData, Map<String, String> pdc, int amount) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
        this.pdc = pdc;
        this.amount = amount <= 0 ? 1 : amount;
    }

    public String getId() {
        return id;
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

    public Map<String, String> getPdc() {
        return pdc;
    }

    public int getAmount() {
        return amount;
    }
}
