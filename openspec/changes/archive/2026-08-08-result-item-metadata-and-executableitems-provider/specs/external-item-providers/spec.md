## MODIFIED Requirements

### Requirement: Namespace Based Item Identifier Resolution
The system SHALL resolve item references formatted as `namespace:item_id` by delegating to the registered provider for that namespace, supporting `craftorithm`, `executableitems` (and `ei` alias), and vanilla `minecraft` namespaces.

#### Scenario: Resolving Craftorithm items
- **WHEN** a recipe specifies an item ID with `crafthorim:ruby_ingot`
- **THEN** the system delegates to the `CraftorithmItemProvider` to construct or match the corresponding `ItemStack`.

#### Scenario: Resolving ExecutableItems items
- **WHEN** a recipe or fuel specifies an item ID with `executableitems:<id>` or `ei:<id>`
- **THEN** the system delegates to the `ExecutableItemsItemProvider` to construct or match the corresponding `ItemStack`.

#### Scenario: Resolving local or vanilla items
- **WHEN** a recipe specifies a vanilla material or local custom item definition without a third-party namespace
- **THEN** the system matches using Bukkit material properties, MiniMessage display names, and PDC tags.

## ADDED Requirements

### Requirement: Soft Dependency Auto-Registration for ExecutableItems
The system SHALL detect the presence of the ExecutableItems plugin during startup and automatically register `ExecutableItemsItemProvider` into `ItemResolverRegistry` under `executableitems` and `ei` namespaces.

#### Scenario: ExecutableItems plugin enabled
- **WHEN** the ExecutableItems plugin is enabled on the server
- **THEN** `ExecutableItemsItemProvider` is registered and available for recipe item matching and resolution.
