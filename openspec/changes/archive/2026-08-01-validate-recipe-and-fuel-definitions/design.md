## Context

Currently, `RecipeManager` and `FuelManager` parse custom YAML files from `recipes/` and `fuels/` directories.
When parsing recipe items (`input` and `result`), `RecipeManager` attempts `Material.matchMaterial(matStr)`. If `matStr` is misspelled (e.g. `GLOD_INGOT`), Bukkit returns `null`. `parseItemDef` constructs a `RecipeItemDefinition` with `material = null`. Later parsing or matching stages silently skip or fail without printing diagnostic details to console.
Similarly, `FuelManager` accepts any `type` string (e.g. `COAAL`) without validating if it maps to a valid Bukkit `Material` or registered `ItemProvider` ID.

## Goals / Non-Goals

**Goals:**
- Implement validation helper methods in `RecipeManager` and `FuelManager` (or `ItemResolverRegistry`) to check string identifiers against `Material.matchMaterial(...)` and registered `ItemProvider` namespaces.
- Log clear, context-rich `WARNING` console messages whenever an invalid material or provider reference is encountered during file parsing.
- Print load summaries reporting the total loaded vs. skipped recipe and fuel counts.

**Non-Goals:**
- Schema changes to custom recipe or fuel YAML formats.
- Automatic fuzzy correction or spelling guessing of misspelled materials.

## Decisions

1. **Validation during file parsing rather than runtime evaluation**:
   - *Decision*: Validate items immediately when loading YAML files on plugin load/reload.
   - *Rationale*: Catches administrative errors early during server startup or `/furnace reload`, preventing runtime failures when players attempt to use furnaces.

2. **Validation Logic Strategy**:
   - For an item definition, check if `id` is present (e.g., `namespace:item_id`). Verify namespace registration in `ItemResolverRegistry`.
   - If `id` is not specified, check if `material` string is present. Validate via `Material.matchMaterial(materialStr)`.
   - If `materialStr != null` but `Material.matchMaterial(materialStr) == null`, log a warning with filename, key, field name (`input.material` or `result.material`), and value (`GLOD_INGOT`).
   - For custom fuels (`type`), check if `type` contains `:` (provider ID) or corresponds to a valid `Material`.

3. **Skipping Invalid Entries**:
   - If a recipe's `input` or `result` fails validation, skip registering that recipe, increment `skippedCount`, and log a warning explaining why.

## Risks / Trade-offs

- **[Risk]** Third-party item plugins (like Oraxen or ItemsAdder) might load *after* PlayerFurnaces in plugin load order.
  - *Mitigation*: Ensure `ItemResolverRegistry` namespace checks gracefully handle standard Bukkit materials and log provider names cleanly. If `id` is specified with a namespace, verify provider presence and issue a clear warning if the provider plugin is not enabled/loaded.
