package dev.darkblade.playerfurnaces;

import dev.darkblade.playerfurnaces.util.ColorUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    @Test
    void testDirectHexParsing() {
        String result = ColorUtils.colorize("#FB5607SIN COMBUSTIBLE");
        assertNotNull(result);
        assertFalse(result.contains("#FB5607"), "Raw #FB5607 should be parsed into legacy section hex code");
        assertTrue(result.contains("SIN COMBUSTIBLE"));
    }

    @Test
    void testAmpersandHexParsing() {
        String result = ColorUtils.colorize("&#FB5607SIN COMBUSTIBLE");
        assertNotNull(result);
        assertFalse(result.contains("&#FB5607"), "Raw &#FB5607 should be parsed into legacy section hex code");
        assertTrue(result.contains("SIN COMBUSTIBLE"));
    }

    @Test
    void testMiniMessageHexParsing() {
        String result = ColorUtils.colorize("<#FB5607>SIN COMBUSTIBLE");
        assertNotNull(result);
        assertFalse(result.contains("<#FB5607>"), "MiniMessage tag <#FB5607> should be serialized");
        assertTrue(result.contains("SIN COMBUSTIBLE"));
    }

    @Test
    void testMixedLegacyAndHexParsing() {
        String result = ColorUtils.colorize("&c#FB5607TIEMPO RESTANTE");
        assertNotNull(result);
        assertFalse(result.contains("#FB5607"), "HEX code should be parsed even when mixed with legacy ampersand codes");
        assertTrue(result.contains("TIEMPO RESTANTE"));
    }
}
