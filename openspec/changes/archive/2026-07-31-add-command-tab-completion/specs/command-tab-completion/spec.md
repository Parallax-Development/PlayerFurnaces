## ADDED Requirements

### Requirement: Player furnace command tab completion
The system SHALL provide tab completion suggestions for the `/furnace` command based on player permissions and matching inputs.

#### Scenario: Player requests tab completion for furnace ID
- **WHEN** a player types `/furnace ` and presses Tab
- **THEN** the system returns a list of valid furnace ID strings (1 through max configured furnaces) for which the player has permission `playerfurnaces.furnace.<id>`

#### Scenario: Player types partial furnace ID
- **WHEN** a player types `/furnace 1` and presses Tab
- **THEN** the system filters suggestions to only include furnace IDs starting with "1" (e.g., 1, 10, 11, 12, 13, 14)

### Requirement: Admin command tab completion
The system SHALL provide tab completion suggestions for the `/playerfurnacesadmin` command based on subcommands, online players, and furnace IDs.

#### Scenario: Admin requests first argument completion
- **WHEN** a player with `playerfurnaces.admin` permission types `/playerfurnacesadmin ` and presses Tab
- **THEN** the system returns subcommands `["reload", "view"]` matching the entered prefix

#### Scenario: Admin requests second argument completion for view subcommand
- **WHEN** an admin types `/playerfurnacesadmin view ` and presses Tab
- **THEN** the system returns online player names matching the entered prefix

#### Scenario: Admin requests third argument completion for view subcommand
- **WHEN** an admin types `/playerfurnacesadmin view <player> ` and presses Tab
- **THEN** the system returns furnace ID numbers (1 through max configured furnaces) matching the entered prefix
