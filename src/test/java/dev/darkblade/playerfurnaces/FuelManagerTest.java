package dev.darkblade.playerfurnaces;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FuelManagerTest {

    @Test
    public void testRecursiveFuelYamlFileDiscovery(@TempDir Path tempFolder) throws IOException {
        Path fuelsDir = tempFolder.resolve("fuels");
        Path subDir1 = fuelsDir.resolve("custom");
        Path subDir2 = fuelsDir.resolve("custom/magic");

        Files.createDirectories(subDir2);

        Path rootFuel = fuelsDir.resolve("coal.yml");
        Path nestedFuel1 = subDir1.resolve("super_coal.yaml");
        Path nestedFuel2 = subDir2.resolve("mana_crystal.yml");
        Path nonYamlFile = subDir1.resolve("readme.md");

        Files.writeString(rootFuel, "coal: {type: COAL, burn-time-ticks: 1600}");
        Files.writeString(nestedFuel1, "super_coal: {type: COAL_BLOCK, burn-time-ticks: 16000}");
        Files.writeString(nestedFuel2, "mana_crystal: {type: BLAZE_ROD, burn-time-ticks: 2400}");
        Files.writeString(nonYamlFile, "info text");

        List<File> discovered = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(fuelsDir)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> {
                        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
                        return name.endsWith(".yml") || name.endsWith(".yaml");
                    })
                    .map(Path::toFile)
                    .forEach(discovered::add);
        }

        assertEquals(3, discovered.size());
        assertTrue(discovered.contains(rootFuel.toFile()));
        assertTrue(discovered.contains(nestedFuel1.toFile()));
        assertTrue(discovered.contains(nestedFuel2.toFile()));
        assertFalse(discovered.contains(nonYamlFile.toFile()));
    }
}
