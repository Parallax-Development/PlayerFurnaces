## ADDED Requirements

### Requirement: Console can forcefully open player furnaces
The system MUST provide a /pfa force-open <player> <furnaceIndex> [--bypass-perms] command for the console to forcefully open a player's furnace.

#### Scenario: Normal usage with bypass flag
- **WHEN** console executes /pfa force-open targetPlayer 2 --bypass-perms
- **THEN** any open inventory for targetPlayer is closed, and their furnace 2 is opened, ignoring permission requirements.

#### Scenario: Normal usage without bypass flag when lacking permission
- **WHEN** console executes /pfa force-open targetPlayer 2 and the player lacks permission for furnace 2
- **THEN** the furnace is not opened, and an error message is sent to the console.

#### Scenario: Normal usage without bypass flag when having permission
- **WHEN** console executes /pfa force-open targetPlayer 2 and the player has permission for furnace 2
- **THEN** any open inventory is closed, and furnace 2 is opened.

#### Scenario: Index out of bounds
- **WHEN** console requests an index that is outside the configured max furnaces limit
- **THEN** the command strictly fails and returns an error without opening or creating the furnace.

#### Scenario: Target player is offline
- **WHEN** the target player is offline
- **THEN** the command fails and returns an offline error.

#### Scenario: Target player is dead
- **WHEN** the target player is online but currently dead
- **THEN** the command fails safely to prevent crashes or dupes.
