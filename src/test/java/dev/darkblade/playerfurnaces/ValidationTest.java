package dev.darkblade.playerfurnaces;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    @Test
    public void testMaterialMatchingValidation() {
        Material validMat = Material.matchMaterial("RAW_IRON");
        assertNotNull(validMat);
        assertEquals(Material.RAW_IRON, validMat);

        Material invalidMat = Material.matchMaterial("GLOD_INGOT");
        assertNull(invalidMat, "GLOD_INGOT should fail validation and return null");
    }

    @Test
    public void testItemProviderNamespaceExtraction() {
        String customId = "craftorithm:ruby_ingot";
        assertTrue(customId.contains(":"));
        String namespace = customId.split(":", 2)[0].trim();
        assertEquals("craftorithm", namespace);
    }
}
