package dev.darkblade.playerfurnaces;

import dev.darkblade.playerfurnaces.model.RecipeItemDefinition;
import dev.darkblade.playerfurnaces.util.RecipeItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeItemBuilderTest {

    @Test
    public void testRecipeItemBuilderMetadataApplication() {
        RecipeItemDefinition def = new RecipeItemDefinition(
                null,
                Material.DIAMOND_SWORD,
                "<red>Excalibur",
                List.of("<gray>Legendary Blade", "<yellow>Damage +100"),
                1005,
                Map.of("myplugin:item_type", "legendary", "tier", "5"),
                2
        );

        assertEquals("<red>Excalibur", def.getName());
        assertEquals(2, def.getLore().size());
        assertEquals(1005, def.getCustomModelData());
        assertEquals("legendary", def.getPdc().get("myplugin:item_type"));

        ItemStack item = RecipeItemBuilder.build(def, null);

        assertNotNull(item);
        assertEquals(Material.DIAMOND_SWORD, item.getType());
        assertEquals(2, item.getAmount());
    }
}
