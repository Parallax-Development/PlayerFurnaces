## ADDED Requirements

### Requirement: Global Vanilla Recipe Toggle
The system SHALL support a global configuration option `recipes.vanilla-smelting.enabled` to control whether vanilla Bukkit smelting recipes can be processed in custom furnaces.

#### Scenario: Vanilla recipes globally disabled
- **WHEN** `recipes.vanilla-smelting.enabled` is set to `false` and an input item without a custom recipe is placed in a custom furnace
- **THEN** the furnace SHALL NOT process or smelt the item.

#### Scenario: Vanilla recipes globally enabled
- **WHEN** `recipes.vanilla-smelting.enabled` is set to `true` and an input item without a custom recipe or blacklist rule is placed in a custom furnace
- **THEN** the furnace SHALL process the item using standard vanilla smelting rules.

### Requirement: Vanilla Material Blacklist
The system SHALL support a configuration list `recipes.vanilla-smelting.disabled-materials` containing material names that must not be smelted via vanilla fallback.

#### Scenario: Input item in material blacklist
- **WHEN** an input item whose material is listed in `disabled-materials` is placed in a custom furnace without a custom recipe
- **THEN** the furnace SHALL NOT process or smelt the item.

### Requirement: Recipe File Modular Disabling
The system SHALL allow custom recipe files (`recipes/*.yml`) to specify `disabled: true` for an input item definition to explicitly disable smelting for that item.

#### Scenario: Input item matches a disabled custom recipe
- **WHEN** an input item matches a custom recipe configured with `disabled: true`
- **THEN** the furnace SHALL NOT process or smelt the item, regardless of global vanilla settings.
