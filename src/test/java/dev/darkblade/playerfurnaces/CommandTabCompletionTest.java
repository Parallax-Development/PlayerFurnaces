package dev.darkblade.playerfurnaces;

import org.bukkit.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTabCompletionTest {

    @Test
    public void testStringUtilPartialMatching() {
        List<String> subcommands = List.of("reload", "view");
        List<String> matches = StringUtil.copyPartialMatches("v", subcommands, new ArrayList<>());

        assertEquals(1, matches.size());
        assertEquals("view", matches.get(0));
    }

    @Test
    public void testFurnaceIdPartialMatching() {
        List<String> ids = List.of("1", "2", "3", "10", "11", "12", "13", "14");
        List<String> matches = StringUtil.copyPartialMatches("1", ids, new ArrayList<>());

        assertEquals(6, matches.size());
        assertTrue(matches.contains("1"));
        assertTrue(matches.contains("10"));
        assertTrue(matches.contains("11"));
        assertTrue(matches.contains("12"));
        assertTrue(matches.contains("13"));
        assertTrue(matches.contains("14"));
    }
}
