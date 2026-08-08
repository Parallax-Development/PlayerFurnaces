## 1. ExecutableItems Provider Integration

- [x] 1.1 Create `ExecutableItemsItemProvider` in `dev.darkblade.playerfurnaces.provider.impl` supporting `executableitems` and `ei` namespaces via reflection.
- [x] 1.2 Register `ExecutableItemsItemProvider` in `PlayerFurnacesPlugin.onEnable()`.

## 2. Result Item Metadata Resolution & Building

- [x] 2.1 Create `RecipeItemBuilder` helper in `dev.darkblade.playerfurnaces.util` to build `ItemStack` from `RecipeItemDefinition` and apply `name` (colorized), `lore` (colorized), `custom-model-data`, and `pdc`.
- [x] 2.2 Update `FurnaceEngine` result item resolution to use `RecipeItemBuilder` so metadata overrides are applied to recipe output items.

## 3. Testing & Verification

- [x] 3.1 Create unit test in `RecipeItemBuilderTest` verifying metadata application (`name`, `lore`, `custom-model-data`, `pdc`) on result `ItemStack`.
- [x] 3.2 Execute `./gradlew test` and `./gradlew build` to confirm clean test and compilation success.
