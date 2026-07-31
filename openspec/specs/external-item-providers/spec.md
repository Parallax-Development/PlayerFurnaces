# external-item-providers Specification

## Purpose
TBD - created by archiving change add-recipe-overrides. Update Purpose after archive.
## Requirements
### Requirement: Namespace Based Item Identifier Resolution
The system SHALL resolve item references formatted as `namespace:item_id` by delegating to the registered provider for that namespace.

#### Scenario: Resolving Craftorithm items
- **WHEN** a recipe specifies an item ID with `crafthorim:ruby_ingot`
- **THEN** the system delegates to the `CraftorithmItemProvider` to construct or match the corresponding `ItemStack`.

#### Scenario: Resolving local or vanilla items
- **WHEN** a recipe specifies a vanilla material or local custom item definition without a third-party namespace
- **THEN** the system matches using Bukkit material properties, MiniMessage display names, and PDC tags.

### Requirement: Extensible ItemProvider Hook Registry
The system SHALL maintain a registry of `ItemProvider` implementations that can be dynamically registered based on soft dependencies on external plugins (e.g. Craftorithm, Oraxen, ItemsAdder).

#### Scenario: Craftorithm plugin present on server
- **WHEN** the Craftorithm plugin is enabled on the server
- **THEN** the `CraftorithmItemProvider` is automatically registered into the `ItemResolverRegistry`.

