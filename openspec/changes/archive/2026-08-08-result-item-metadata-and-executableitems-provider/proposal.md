## Why

When custom furnace recipes define result items, `FurnaceEngine` currently resolves the base `ItemStack` from a provider or vanilla material, but fails to apply metadata overrides (`name`, `lore`, `custom-model-data`, `pdc`) specified in the recipe result section. Additionally, ExecutableItems items (`executableitems:id` / `ei:id`) are not registered in `ItemResolverRegistry`, preventing ExecutableItems from being resolved as recipe inputs, fuels, or outputs.

## What Changes

- Add `ExecutableItemsItemProvider` supporting `executableitems` and `ei` namespaces via reflection/soft-depend.
- Register `ExecutableItemsItemProvider` in `PlayerFurnacesPlugin` on startup.
- Update `FurnaceEngine` to construct a complete result `ItemStack` using `RecipeItemBuilder` / metadata applier that formats `name` (MiniMessage / HEX / &), `lore`, `custom-model-data`, and `pdc` tags defined in the `result` recipe section onto both vanilla and provider-generated result items.

## Capabilities

### New Capabilities
<!-- None -->

### Modified Capabilities
- `recipe-overrides`: Update custom recipe smelting execution requirement so result items retain and apply defined `name`, `lore`, `custom-model-data`, and `pdc` metadata.
- `external-item-providers`: Extend namespace item resolution to support `executableitems` and `ei` namespaces.

## Impact

- `dev.darkblade.playerfurnaces.engine.FurnaceEngine`: Update result item resolution and metadata application.
- `dev.darkblade.playerfurnaces.provider.impl.ExecutableItemsItemProvider`: New item provider implementation for ExecutableItems plugin.
- `dev.darkblade.playerfurnaces.PlayerFurnacesPlugin`: Register `ExecutableItemsItemProvider` into `ItemResolverRegistry`.
- `dev.darkblade.playerfurnaces.util.RecipeItemBuilder`: Utility method or helper to apply `RecipeItemDefinition` metadata onto an `ItemStack`.
