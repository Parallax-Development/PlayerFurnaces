## MODIFIED Requirements

### Requirement: Administrator Inspection & Management
The system SHALL provide administrative commands under `/pfadmin` to inspect, manage, force-open, and reload virtual furnaces. Permission verification for force-opening a furnace SHALL check standard furnace permissions `playerfurnaces.furnace.<id>` unless explicitly bypassed with `--bypass-perms`.

#### Scenario: Admin viewing player furnace
- **WHEN** administrator executes `/pfadmin view <player> <id>`
- **THEN** system opens specified player's virtual furnace GUI for inspection or modification regardless of whether the player is online or offline

#### Scenario: Admin forcing player to open default furnace
- **WHEN** administrator executes `/pfadmin force-open <player> 1` for an online player with default permissions
- **THEN** system verifies permission via `playerfurnaces.furnace.1` and forces the target player to open furnace #1 without error
