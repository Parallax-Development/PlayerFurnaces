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

public class RecipeManagerTest {

    @Test
    public void testRecursiveYamlFileDiscovery(@TempDir Path tempFolder) throws IOException {
        Path recipesDir = tempFolder.resolve("recipes");
        Path subDir1 = recipesDir.resolve("armors");
        Path subDir2 = recipesDir.resolve("armors/netherite");

        Files.createDirectories(subDir2);

        Path rootRecipe = recipesDir.resolve("example.yml");
        Path nestedRecipe1 = subDir1.resolve("copper_armor.yaml");
        Path nestedRecipe2 = subDir2.resolve("super_netherite.yml");
        Path nonYamlFile = subDir1.resolve("notes.txt");

        Files.writeString(rootRecipe, "recipe1: {input: {material: COAL}, result: {material: DIAMOND}}");
        Files.writeString(nestedRecipe1, "recipe2: {input: {material: IRON_ORE}, result: {material: IRON_INGOT}}");
        Files.writeString(nestedRecipe2, "recipe3: {input: {material: GOLD_ORE}, result: {material: GOLD_INGOT}}");
        Files.writeString(nonYamlFile, "ignore this file");

        List<File> discovered = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(recipesDir)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> {
                        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
                        return name.endsWith(".yml") || name.endsWith(".yaml");
                    })
                    .map(Path::toFile)
                    .forEach(discovered::add);
        }

        assertEquals(3, discovered.size());
        assertTrue(discovered.contains(rootRecipe.toFile()));
        assertTrue(discovered.contains(nestedRecipe1.toFile()));
        assertTrue(discovered.contains(nestedRecipe2.toFile()));
        assertFalse(discovered.contains(nonYamlFile.toFile()));
    }
}
