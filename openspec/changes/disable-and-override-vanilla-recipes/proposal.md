## Why

Currently, PlayerFurnaces automatically falls back to vanilla Bukkit smelting recipes for any input item that doesn't match a custom recipe file. Server administrators lack the ability to globally disable vanilla smelting in custom furnaces or blacklist specific vanilla materials, preventing full control over the furnace economy and recipe balances.

## What Changes

- Add global configuration (`recipes.vanilla-smelting.enabled`) to enable or disable vanilla recipe fallback across custom furnaces.
- Add configuration option (`recipes.vanilla-smelting.disabled-materials`) to specify a list of vanilla materials that cannot be smelted.
- Add support for a `disabled: true` flag in custom recipe YML files (`recipes/*.yml`) to allow modular disabling of specific input items via custom recipe files.
- Refactor recipe lookup in `FurnaceEngine` to respect global, material-level, and custom recipe-level disabling rules before falling back to vanilla smelting.

## Capabilities

### New Capabilities
- `vanilla-recipe-control`: Configuration and logic to disable, blacklist, or selectively override vanilla smelting recipes in custom furnaces.

### Modified Capabilities

## Impact

- `config.yml`: New configuration keys under `recipes.vanilla-smelting`.
- `CustomRecipe.java`: New `disabled` field.
- `RecipeManager.java`: Parser support for `disabled` flag in custom recipe files.
- `FurnaceEngine.java`: Updated smelting evaluation loop to apply vanilla recipe restrictions.
