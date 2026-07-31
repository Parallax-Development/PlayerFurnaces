## Why

Currently, PlayerFurnaces only supports vanilla Minecraft furnace recipes fetched dynamically from Bukkit's recipe iterator, and hardcoded fuel burn times. Server administrators cannot define custom smelting recipes (custom inputs/outputs) or integrate items from third-party plugins like Craftorithm, Oraxen, ItemsAdder, etc., limiting custom economy and RPG workflows.

## What Changes

- **1 Recipe Per File System**: Support loading custom furnace recipes from individual `.yml` files in a `recipes/` directory.
- **External Plugin & Item Provider Integration**: Support resolving item identifiers with the `namespace:item_id` syntax (e.g. `crafthorim:ruby_ingot`, `oraxen:hyper_coal`, `minecraft:iron_ingot`) via an extensible ItemProvider adapter registry.
- **Custom Local Item Definitions**: Support defining local custom items directly in YAML using material, MiniMessage/color display name, lore, custom model data, and PersistentDataContainer (PDC) key-value tags.
- **Global & Recipe-Specific Fuels**: Support registering global fuels (in a `fuels/` directory) and specifying allowed fuel restrictions or custom burn times per recipe.
- **Engine Recipe Matcher Update**: Upgrade `SmeltingManager` and `FurnaceEngine` to match furnace slot items against custom recipe overrides before falling back to vanilla Bukkit recipes.

## Capabilities

### New Capabilities
- `recipe-overrides`: Core recipe engine supporting custom YAML recipes (inputs, results, cook times, experience) and global/recipe-specific fuel configurations.
- `external-item-providers`: Adapter architecture for resolving and matching items from third-party plugins (Craftorithm, Oraxen, etc.) using `namespace:item_id` format.

### Modified Capabilities
*(None - no existing main specs in openspec/specs/)*

## Impact

- **Codebase**: `SmeltingManager`, `FurnaceEngine`, `VirtualFurnace`, and plugin startup initialization in `PlayerFurnacesPlugin`.
- **New Components**: `RecipeManager`, `FuelManager`, `ItemResolverRegistry`, `CraftorithmItemProvider`, YAML configuration parsers.
- **Config & File System**: New directories `recipes/` and `fuels/` inside `plugins/PlayerFurnaces/`.
