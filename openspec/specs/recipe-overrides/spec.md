# recipe-overrides Specification

## Purpose
Specification for custom recipe configuration loading, item matching, fuel restrictions, and validation.
## Requirements
### Requirement: Modular YAML Recipe Loading
The system SHALL recursively load custom furnace recipe override files from the `plugins/PlayerFurnaces/recipes/` directory and any of its subdirectories, treating each `.yml` and `.yaml` file as a distinct recipe definition.

#### Scenario: Loading recipe files on startup
- **WHEN** the plugin starts or executes `/furnace reload`
- **THEN** all valid `.yml` and `.yaml` files inside `plugins/PlayerFurnaces/recipes/` and its subdirectories are parsed into custom recipe models and registered in the internal recipe registry.

#### Scenario: Loading recipe files in nested subfolders
- **WHEN** custom recipe YAML files exist inside subdirectories of `plugins/PlayerFurnaces/recipes/` (e.g. `recipes/armors/netherite.yml`)
- **THEN** the system recursively traverses the directory tree and registers all valid recipe files.

### Requirement: Custom Recipe Material and Provider Validation
The system SHALL validate input and result material names and third-party item provider IDs during recipe file loading, logging a descriptive warning and skipping registration for any recipe with invalid material or provider references.

#### Scenario: Recipe with misspelled material name
- **WHEN** a recipe YAML file contains an unrecognized material name in `input` or `result` (e.g., `GLOD_INGOT`)
- **THEN** the system logs a `WARNING` in the server log specifying the file name, recipe ID, section, and invalid material name, and skips registering that recipe.

#### Scenario: Recipe with invalid item provider ID
- **WHEN** a recipe YAML file contains an unrecognized item provider namespace in `id` (e.g., `unknownplugin:item_id`)
- **THEN** the system logs a `WARNING` in the server log specifying the file name, recipe ID, section, and unresolved item provider ID, and skips registering that recipe.

#### Scenario: Summary of recipe loading
- **WHEN** custom recipes finish loading during plugin startup or reload
- **THEN** the system logs the count of successfully registered recipes as well as the count of recipes skipped due to configuration errors.

### Requirement: Custom Recipe Matching and Smelting Execution
The system SHALL prioritize custom recipe overrides over standard vanilla Bukkit recipes during furnace ticking.

#### Scenario: Smelting an input item matching a custom recipe
- **WHEN** a furnace input slot contains an item matching a custom recipe override's `input`
- **THEN** the furnace smelts the item using the recipe's specified `cook-time-ticks` and produces the defined `result` item and `experience`.

### Requirement: Recipe Fuel Restrictions and Overrides
The system SHALL support recipe-level fuel restrictions and custom burn time definitions.

#### Scenario: Smelting with a restricted fuel type
- **WHEN** a custom recipe defines a `fuel` constraint
- **THEN** the furnace only ignites and smelts when the fuel slot contains an item matching the allowed fuel specification and burns for the specified `burn-time-ticks`.

