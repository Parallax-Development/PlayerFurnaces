## Context

Currently, `SmeltingManager` uses Bukkit's standard `recipeIterator()` to match furnace inputs against vanilla recipes. Fuel burn times are hardcoded in a static `HashMap<Material, Integer>`.
This architecture prevents servers from introducing custom items (e.g., Craftorithm, Oraxen, custom PDC/MiniMessage items) into furnace inputs/outputs or defining custom fuels.

## Goals / Non-Goals

**Goals:**
- Implement a modular 1-recipe-per-file YAML loader inside `plugins/PlayerFurnaces/recipes/`.
- Implement a global & recipe-specific fuel loader inside `plugins/PlayerFurnaces/fuels/`.
- Provide an extensible `ItemProvider` adapter registry supporting `namespace:item_id` (specifically `CraftorithmItemProvider` for Craftorithm).
- Update `SmeltingManager` and `FurnaceEngine` to resolve custom recipes before falling back to vanilla recipes.

**Non-Goals:**
- Creating a GUI recipe editor in-game (recipes will be managed via YAML files).
- Modifying vanilla furnace behavior outside of PlayerFurnaces' virtual furnace engine.

## Decisions

### Decision 1: Namespace-Based Item Identification (`namespace:item_id`)
- **Rationale**: Using a clean `namespace:item_id` format allows simple, unambiguous dispatching to external plugin APIs (e.g. `crafthorim:ruby_ingot`, `oraxen:hyper_coal`, `minecraft:iron_ingot`).
- **Alternatives Considered**:
  - *Full ItemStack Serialization*: Harder for admins to edit manually.
  - *Hardcoded Plugin Checks*: Messy `if (hasOraxen) ... else if (hasCraftorithm)` code spread throughout the engine.

### Decision 2: Prioritized Matcher Chain in `SmeltingManager`
- **Rationale**: Checking Custom Recipe Overrides first allows admins to override standard vanilla recipes (e.g. smelting raw iron into a custom refined iron ingot). Fallback to `Bukkit.recipeIterator()` ensures standard vanilla smelting continues to work seamlessly.

## Risks / Trade-offs

- **[Risk] Soft Dependency Availability**: If a recipe references `crafthorim:ruby_ingot` but Craftorithm is not installed/enabled.
  - **Mitigation**: Log a clear warning during recipe loading, mark the recipe as inactive/disabled, and skip registration gracefully without crashing the engine.
- **[Risk] Item Comparison Performance**: Matching NBT/PDC and MiniMessage display names on every tick.
  - **Mitigation**: Pre-compile and index recipes by `Material` or namespace key upon file load.

## Migration Plan

1. Create default `recipes/` and `fuels/` folders on plugin startup if missing, creating `example.yml` as a reference.
2. Existing virtual furnaces continue working without database schema changes because `VirtualFurnace` operates on standard `ItemStack` slots.
