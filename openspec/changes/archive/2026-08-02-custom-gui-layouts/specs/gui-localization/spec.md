## MODIFIED Requirements

### Requirement: Externalized GUI Titles and Item Text
The system SHALL resolve all GUI titles, item names, status descriptions, and lore lists from `menus.yml` using a specialized layout or menu configuration manager.

#### Scenario: Opening Furnace Hub GUI
- **WHEN** a player opens the virtual furnace hub GUI
- **THEN** the title and each furnace icon (Locked, Smelting, No Fuel, Idle) display strings and lore loaded from the layout configuration in `menus.yml`.

#### Scenario: Opening Furnace View GUI
- **WHEN** a player opens an individual furnace GUI
- **THEN** progress indicators, fuel indicators, collect button, and back button display strings loaded from the layout configuration in `menus.yml`.
