# gui-localization Specification

## Purpose
TBD - created by archiving change gui-messages-localization. Update Purpose after archive.
## Requirements
### Requirement: Externalized GUI Titles and Item Text
The system SHALL resolve all GUI titles, item names, status descriptions, and lore lists from `messages.yml` using `MessageManager`.

#### Scenario: Opening Furnace Hub GUI
- **WHEN** a player opens the virtual furnace hub GUI
- **THEN** the title and each furnace icon (Locked, Smelting, No Fuel, Idle) display localized English strings and lore loaded from `messages.yml`.

#### Scenario: Opening Furnace View GUI
- **WHEN** a player opens an individual furnace GUI
- **THEN** progress indicators, fuel indicators, collect button, and back button display localized English strings loaded from `messages.yml`.

### Requirement: English as Primary Default Language
The default `messages.yml` provided in the plugin JAR resources SHALL be written in English.

#### Scenario: First plugin startup
- **WHEN** the plugin generates `messages.yml` for the first time
- **THEN** all default keys for messages and GUIs are initialized in English.

