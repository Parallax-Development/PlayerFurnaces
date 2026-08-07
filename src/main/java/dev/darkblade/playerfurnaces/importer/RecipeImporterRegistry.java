package dev.darkblade.playerfurnaces.importer;

import java.util.*;

public class RecipeImporterRegistry {
    private final Map<String, RecipeImporter> importers = new LinkedHashMap<>();

    public void registerImporter(RecipeImporter importer) {
        importers.put(importer.getPluginName().toLowerCase(Locale.ROOT), importer);
    }

    public RecipeImporter getImporter(String pluginName) {
        if (pluginName == null) return null;
        return importers.get(pluginName.toLowerCase(Locale.ROOT));
    }

    public List<String> getAvailablePluginNames() {
        List<String> available = new ArrayList<>();
        for (RecipeImporter importer : importers.values()) {
            if (importer.isAvailable()) {
                available.add(importer.getPluginName().toLowerCase(Locale.ROOT));
            }
        }
        return available;
    }

    public Collection<RecipeImporter> getAllImporters() {
        return Collections.unmodifiableCollection(importers.values());
    }
}
