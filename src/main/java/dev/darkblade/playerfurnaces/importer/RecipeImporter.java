package dev.darkblade.playerfurnaces.importer;

import java.io.File;

public interface RecipeImporter {
    String getPluginName();
    boolean isAvailable();
    ImportResult importRecipes(File baseRecipesDir, boolean overwrite);
}
