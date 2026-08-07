# Design Document: Recipe Import Command

## Overview

This feature introduces an extensible importer framework for PlayerFurnaces to import smelting recipes from external plugins, starting with Craftorithm.

## Architecture

### Components

1. **`RecipeImporter` Interface (`dev.darkblade.playerfurnaces.importer.RecipeImporter`)**:
   - `String getPluginName()`
   - `boolean isAvailable()`
   - `ImportResult importRecipes(File baseRecipesDir, boolean overwrite)`

2. **`RecipeImporterRegistry` (`dev.darkblade.playerfurnaces.importer.RecipeImporterRegistry`)**:
   - Manages registered `RecipeImporter` instances.
   - Provides methods to retrieve available importers for tab completion and command execution.

3. **`CraftorithmRecipeImporter` (`dev.darkblade.playerfurnaces.importer.impl.CraftorithmRecipeImporter`)**:
   - Implements `RecipeImporter` for Craftorithm.
   - Safe reflection calls to fetch registered recipes from `cc.sy.craftorithm.api.CraftorithmAPI` / `CraftorithmPlugin`.
   - Filters furnace / smelting recipes.
   - Generates YAML format in `recipes/craftorithm/<recipe_id>.yml`.

4. **`AdminCommand` (`dev.darkblade.playerfurnaces.command.AdminCommand`)**:
   - Handles `import` subcommand and arguments: `/pfa import <plugin> [--overwrite|-f]`.
   - Tab completes enabled importers and flags.
   - Executes `importer.importRecipes(...)` and then triggers `plugin.getRecipeManager().loadRecipes()`.

## Data Flow

```
[Command Sender] -> /pfa import craftorithm --overwrite
  -> AdminCommand validates permissions & arguments
  -> RecipeImporterRegistry finds CraftorithmRecipeImporter
  -> CraftorithmRecipeImporter queries Craftorithm API
  -> Writes YAMLs into plugins/PlayerFurnaces/recipes/craftorithm/
  -> Calls RecipeManager.loadRecipes()
  -> Sends summary message to Command Sender
```
