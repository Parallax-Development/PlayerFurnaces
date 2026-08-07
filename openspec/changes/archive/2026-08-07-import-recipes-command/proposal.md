## Why

Administrators currently have to write custom recipe YAML files manually in `plugins/PlayerFurnaces/recipes/` when integrating third-party items and recipes from plugins like Craftorithm. Adding an admin import command `/pfa import <plugin>` automates recipe discovery and format conversion, making integration seamless.

## What Changes

- Add sub-command `/pfa import <plugin> [--overwrite|-f]` to the admin command handler (`/pfa` or `/playerfurnacesadmin`).
- Implement an extensible `RecipeImporterManager` and `RecipeImporter` interface.
- Implement `CraftorithmRecipeImporter` using Java reflection to access Craftorithm's recipe registry API and extract furnace/smelting recipes.
- Save imported recipes as individual YAML files in subfolders per plugin (`recipes/<plugin>/<recipe_id>.yml`).
- Automatically trigger `RecipeManager.loadRecipes()` after importing so new recipes take effect immediately.
- Add tab completion for the `import` subcommand, suggesting installed and enabled importable plugins as well as `--overwrite`.

## Capabilities

### New Capabilities
- `recipe-import`: Capability to import custom furnace recipes from third-party plugins like Craftorithm into PlayerFurnaces recipe YAML format.

### Modified Capabilities
- None

## Impact

- Command System (`AdminCommand.java`): New subcommand and tab completion.
- Recipe Management (`RecipeManager.java`): Hot reload called post-import.
- Third-party dependencies: Dynamic integration with Craftorithm via reflection/API.
