## Why

Imported Craftorithm recipes currently encounter namespace mismatches (`craftorithm` vs `crafthorim`) and incomplete item metadata extraction (such as ignoring `RecipeChoice.MaterialChoice`, display names, lore, custom model data, or custom PDC data). Fixing these ensures imported recipes are parsed accurately by `RecipeManager` and function flawlessly in PlayerFurnaces.

## What Changes

- Update `CraftorithmItemProvider` to register `"craftorithm"` as primary namespace while maintaining `"crafthorim"` as a supported alias.
- Enhance `CraftorithmRecipeImporter` to handle `RecipeChoice.MaterialChoice` inputs alongside `ExactChoice`.
- Preserve full item properties (`material`, `id: "craftorithm:<item_id>"`, `name`, `lore`, `custom-model-data`, `pdc`, `amount`) for both inputs and results.
- Ensure generated YAML files produce keys and structures completely compatible with `RecipeManager` validation rules.

## Capabilities

### New Capabilities
- None

### Modified Capabilities
- `recipe-import`: Improve Craftorithm item resolution and recipe extraction precision.

## Impact

- `CraftorithmItemProvider.java`: Add namespace alias support.
- `CraftorithmRecipeImporter.java`: Comprehensive item metadata extraction and correct namespace generation.
