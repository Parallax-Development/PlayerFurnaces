## ADDED Requirements

### Requirement: Character-Based GUI Layout Engine
The system SHALL parse visual templates defined as arrays of strings in `menus.yml`, where each string represents a row and each character represents a specific slot assignment in a Bukkit Inventory.

#### Scenario: Rendering a layout from config
- **WHEN** a player opens a GUI
- **THEN** the system loads the layout strings from `menus.yml`, resolves the characters using the defined legend, and places the corresponding items or dynamic markers in a Bukkit Inventory.

### Requirement: Dynamic Furnace Slot Allocation
The system SHALL support dynamic rendering of virtual furnaces within the Hub layout based on a designated placeholder character (e.g., `#`), allocating them sequentially until no more furnaces or placeholders remain.

#### Scenario: Populating multiple furnaces
- **WHEN** the Hub layout contains multiple `#` characters
- **THEN** the system assigns Furnace 1 to the first `#`, Furnace 2 to the second `#`, and so forth.

### Requirement: Unified Visual Configuration
The system SHALL load all visual configurations for menus (titles, layouts, materials, names, and lores) exclusively from `menus.yml`.

#### Scenario: Reading item lore
- **WHEN** generating a menu button (e.g. Collect Button)
- **THEN** the system retrieves its material, display name, and lore from the `legend` section in `menus.yml` rather than hardcoding them in Java.
