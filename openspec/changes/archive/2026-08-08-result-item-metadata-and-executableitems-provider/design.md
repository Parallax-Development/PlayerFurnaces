## Context

Custom recipes in `PlayerFurnaces` support external item providers (`id: "crafthorim:item"`) as well as custom metadata defined in YAML (`name`, `lore`, `custom-model-data`, `pdc`). However:
1. `FurnaceEngine` only called `resolveItem` or created a raw `new ItemStack(material)` without applying metadata defined in `RecipeItemDefinition` onto the result `ItemStack`.
2. `ExecutableItems` was not integrated into `ItemResolverRegistry`, preventing ExecutableItems items from being referenced by `executableitems:<id>` or `ei:<id>`.

## Goals / Non-Goals

**Goals:**
- Provide a `RecipeItemBuilder` helper that builds an `ItemStack` from a `RecipeItemDefinition` and applies all defined `name`, `lore`, `custom-model-data`, and `pdc` metadata (with `ColorUtils` color parsing).
- Update `FurnaceEngine` to use `RecipeItemBuilder` when constructing the furnace result item.
- Implement `ExecutableItemsItemProvider` (with `executableitems` and `ei` namespaces) using reflection on `ExecutableItemsAPI` to safely soft-depend on ExecutableItems.

**Non-Goals:**
- Modifying how input item matching or fuel validation works beyond supporting ExecutableItems resolution.

## Decisions

### Decision 1: Create `RecipeItemBuilder` utility

**Rationale:**
Centralizing item construction from `RecipeItemDefinition` ensures consistent metadata handling across recipe outputs, fuel definitions, and potential future recipe preview GUIs.

**Behavior:**
1. Base item resolution: Try `registry.resolveItem(id, amount)` if `id` contains `:`.
2. Fallback base item: If null, create `new ItemStack(material, amount)`.
3. Metadata decoration:
   - `name`: `meta.setDisplayName(ColorUtils.colorize(name))`
   - `lore`: `meta.setLore(ColorUtils.colorize(lore))`
   - `custom-model-data`: `meta.setCustomModelData(customModelData)`
   - `pdc`: Write key-value pairs to `meta.getPersistentDataContainer()` using `NamespacedKey`.

### Decision 2: Reflection-based `ExecutableItemsItemProvider`

**Rationale:**
Using reflection to invoke `ExecutableItemsAPI.getExecutableItem(id)` prevents hard compile-time dependencies, allowing `PlayerFurnaces` to run cleanly whether ExecutableItems is installed or not.

## Risks / Trade-offs

- **[Risk]** Overriding name/lore on external provider items (e.g. ExecutableItems) might replace the plugin's default item name.
  - **Mitigation:** Only apply `name`, `lore`, `custom-model-data`, or `pdc` if explicitly defined in the recipe YML section. If omitted in YML, preserve the provider item's native metadata.
