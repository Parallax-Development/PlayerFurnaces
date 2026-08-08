## MODIFIED Requirements

### Requirement: Custom Recipe Matching and Smelting Execution
The system SHALL prioritize custom recipe overrides over standard vanilla Bukkit recipes during furnace ticking, and SHALL apply any configured `name`, `lore`, `custom-model-data`, and `pdc` metadata from the recipe's `result` section onto the resulting `ItemStack`.

#### Scenario: Smelting an input item matching a custom recipe
- **WHEN** a furnace input slot contains an item matching a custom recipe override's `input`
- **THEN** the furnace smelts the item using the recipe's specified `cook-time-ticks` and produces the defined `result` item with all configured `name`, `lore`, `custom-model-data`, and `pdc` metadata applied.

#### Scenario: Result item metadata formatting
- **WHEN** a custom recipe defines custom `name` or `lore` on the result item
- **THEN** the system formats color codes (supporting MiniMessage, HEX `#RRGGBB` / `&#RRGGBB`, and legacy `&`) and sets the formatted display name and lore list on the produced item.
