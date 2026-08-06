# virtual-furnaces Specification

## Purpose
TBD - created by archiving change virtual-player-furnaces. Update Purpose after archive.
## Requirements
### Requirement: Virtual Furnace Selector GUI
The system SHALL display an interactive GUI when a player executes `/furnace` or `/horno` showing all virtual furnaces available to that player, utilizing the dynamic layout engine to render the visual structure.

#### Scenario: Opening furnace selector
- **WHEN** player executes `/furnace`
- **THEN** system opens a GUI listing furnaces with visual status indicators (Smelting, Idle, Out of Fuel, Locked) mapped to the dynamic layout slots based on player permissions `playerfurnaces.furnace.<number>`

#### Scenario: Opening locked furnace
- **WHEN** player clicks on a locked furnace icon in the dynamically rendered GUI
- **THEN** system prevents access and sends a message explaining that permission `playerfurnaces.furnace.<number>` is required

### Requirement: Virtual Furnace Smelting & Fuel Mechanics
The system SHALL provide a virtual furnace interface with Input, Fuel, and Output slots that smelts items following standard vanilla recipe times and fuel burn durations.

#### Scenario: Smelting an item
- **WHEN** player places valid input items (e.g., Raw Iron) and valid fuel (e.g., Coal) into a furnace
- **THEN** furnace consumes fuel, displays progress, and yields smelted output (e.g., Iron Ingot) into the output slot

#### Scenario: Offline smelting calculation
- **WHEN** a player reopens a furnace or accesses it after being offline
- **THEN** system calculates elapsed time since last update and processes all pending smelting cycles accurately

### Requirement: Anti-Dupe Item Data Persistence
The system SHALL store furnace state, input items, fuel items, and output items in an H2 or SQLite database using binary item serialization.

#### Scenario: Server restart item persistence
- **WHEN** server restarts or reloads while a furnace contains custom items with NBT/PDC data
- **THEN** system restores furnace contents with exact NBT metadata intact without duplication or item loss

### Requirement: Administrator Inspection & Management
The system SHALL provide administrative commands under `/pfadmin` to inspect, manage, force-open, and reload virtual furnaces. Permission verification for force-opening a furnace SHALL check standard furnace permissions `playerfurnaces.furnace.<id>` unless explicitly bypassed with `--bypass-perms`.

#### Scenario: Admin viewing player furnace
- **WHEN** administrator executes `/pfadmin view <player> <id>`
- **THEN** system opens specified player's virtual furnace GUI for inspection or modification regardless of whether the player is online or offline

#### Scenario: Admin forcing player to open default furnace
- **WHEN** administrator executes `/pfadmin force-open <player> 1` for an online player with default permissions
- **THEN** system verifies permission via `playerfurnaces.furnace.1` and forces the target player to open furnace #1 without error

