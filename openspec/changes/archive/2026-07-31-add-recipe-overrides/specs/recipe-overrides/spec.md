## ADDED Requirements

### Requirement: Modular YAML Recipe Loading
The system SHALL load custom furnace recipe override files from the `plugins/PlayerFurnaces/recipes/` directory, treating each `.yml` file as a distinct recipe definition.

#### Scenario: Loading recipe files on startup
- **WHEN** the plugin starts or executes `/furnace reload`
- **THEN** all valid `.yml` files inside `plugins/PlayerFurnaces/recipes/` are parsed into custom recipe models and registered in the internal recipe registry.

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
