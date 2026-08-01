## Context

PlayerFurnaces processes smelting operations through `FurnaceEngine.java`. When an input item is evaluated:
1. `RecipeManager.findMatchingRecipe(input)` searches for custom recipe matches.
2. If none match, `SmeltingManager.getSmeltingRecipe(input)` resolves Bukkit vanilla smelting recipes.

We need to introduce configurable controls to disable vanilla recipe fallback globally and selectively, while supporting modular disabling within custom recipe files (`recipes/*.yml`).

## Goals / Non-Goals

**Goals:**
- Provide a global toggle (`recipes.vanilla-smelting.enabled`) in `config.yml`.
- Provide a material blacklist (`recipes.vanilla-smelting.disabled-materials`) in `config.yml`.
- Allow recipe YML files to declare `disabled: true` to suppress matching input items.
- Ensure custom recipes continue to override vanilla recipes smoothly.

**Non-Goals:**
- Unregistering or removing vanilla recipes globally from the Minecraft Bukkit server instance itself (the restriction applies specifically to custom PlayerFurnaces).

## Decisions

### 1. Store Vanilla Settings in `RecipeManager` or plugin config
- Read `recipes.vanilla-smelting.enabled` and `recipes.vanilla-smelting.disabled-materials` during plugin load/reload in `RecipeManager`.
- Expose methods `boolean isVanillaSmeltingEnabled()` and `boolean isVanillaMaterialDisabled(Material mat)` on `RecipeManager`.

### 2. Add `disabled` field to `CustomRecipe`
- Update `CustomRecipe` constructor and getters to include `boolean disabled`.
- Update `RecipeManager.parseRecipe` to read `boolean disabled = section.getBoolean("disabled", false);`.
- `result` can be null if `disabled` is true.

### 3. Evaluation Order in `FurnaceEngine`
1. Check `customRecipe = recipeManager.findMatchingRecipe(input)`.
2. If `customRecipe != null`:
   - If `customRecipe.isDisabled()`, reset cook time to 0 and break (do not smelt).
   - Else process custom recipe output.
3. If `customRecipe == null`:
   - If `!recipeManager.isVanillaSmeltingEnabled()` OR `recipeManager.isVanillaMaterialDisabled(input.getType())`, reset cook time to 0 and break.
   - Else proceed to `SmeltingManager.getSmeltingRecipe(input)`.

## Risks / Trade-offs

- [Risk] Material string mismatch in `disabled-materials` configuration list → Mitigation: Parse materials with `Material.matchMaterial(str)` and log a warning if invalid material names are configured.
