## Why

Currently, when a server administrator makes a typo or mistake in custom recipe or fuel configuration files (such as specifying an invalid material `GLOD_INGOT` instead of `GOLD_INGOT`), the plugin fails silently. It omits the invalid entry without printing any warning or error to the server log, leaving administrators unaware of why recipes or fuels are not registering or working in-game.

## What Changes

- Add strict material and item provider ID validation during custom recipe parsing (`RecipeManager`).
- Add strict material and item provider ID validation during custom fuel parsing (`FuelManager`).
- Log explicit `WARNING` messages to the console whenever an invalid material name or unresolved item provider ID is encountered in configuration files, detailing the file name, recipe/fuel key, field name, and invalid value.
- Enhance plugin load summaries to report both successfully loaded items and any entries skipped due to configuration errors.

## Capabilities

### New Capabilities
- `fuel-validation`: Validates fuel type identifiers (materials and third-party item providers) upon loading fuel configurations, logging descriptive console warnings for invalid entries.

### Modified Capabilities
- `recipe-overrides`: Require validation of input and result material names and item provider IDs when loading custom recipe YAML files, logging detailed warning messages and tracking skipped recipes.

## Impact

- Affected code: `RecipeManager.java`, `FuelManager.java`, `ItemMatcher.java`, `ItemResolverRegistry.java`.
- APIs/Behavior: No breaking changes to YAML structure; existing valid configurations will continue to load as expected, while invalid configurations will now produce clear diagnostic warnings in the server console instead of failing silently.
