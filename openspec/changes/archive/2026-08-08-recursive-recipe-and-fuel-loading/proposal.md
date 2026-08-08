## Why

Currently, `RecipeManager` and `FuelManager` use single-level directory listing (`File.listFiles()`) when loading configuration files from `plugins/PlayerFurnaces/recipes/` and `plugins/PlayerFurnaces/fuels/`. Server administrators organizing custom recipes and fuel definitions into subfolders (e.g. `recipes/armors/netherite.yml` or `fuels/custom/magic.yml`) find that items in subfolders are silently ignored, causing unexpected fallback behaviors or un-smeltable items.

## What Changes

- Update `RecipeManager` to recursively scan `plugins/PlayerFurnaces/recipes/` and all subdirectories for `.yml` and `.yaml` recipe files.
- Update `FuelManager` to recursively scan `plugins/PlayerFurnaces/fuels/` and all subdirectories for `.yml` and `.yaml` fuel files.

## Capabilities

### New Capabilities
<!-- None -->

### Modified Capabilities
- `recipe-overrides`: Extend modular YAML recipe loading requirement to recursively scan all subdirectories inside `recipes/`.
- `fuel-validation`: Extend fuel configuration loading requirement to recursively scan all subdirectories inside `fuels/`.

## Impact

- `dev.darkblade.playerfurnaces.manager.RecipeManager`: Update directory file loading logic from `listFiles` to recursive directory scanning.
- `dev.darkblade.playerfurnaces.manager.FuelManager`: Update directory file loading logic from `listFiles` to recursive directory scanning.
- No breaking changes; existing single-level files in `recipes/` and `fuels/` continue to load as expected.
