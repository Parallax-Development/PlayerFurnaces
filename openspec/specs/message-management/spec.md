# message-management Specification

## Purpose
TBD - created by archiving change fix-messages-loading-and-manager. Update Purpose after archive.
## Requirements
### Requirement: Default messages configuration file persistence
The plugin SHALL automatically save `messages.yml` from resources to the plugin data folder during startup if the file does not already exist.

#### Scenario: Plugin startup with missing messages file
- **WHEN** the plugin enables and `messages.yml` is missing in `plugins/PlayerFurnaces/`
- **THEN** `messages.yml` is copied from the plugin resource bundle to the data folder without overwriting existing files.

### Requirement: MessageManager load and reload capability
The `MessageManager` SHALL load all key-value entries from `messages.yml`, translating color codes using `&` and prepending the configured prefix where applicable.

#### Scenario: Executing admin reload command
- **WHEN** an admin executes `/pfadmin reload`
- **THEN** `messages.yml` and `config.yml` are re-read from disk and the `MessageManager` updates its in-memory message cache.

### Requirement: Configurable command and GUI responses
Commands and GUIs SHALL send player-facing output using `MessageManager` instead of hardcoded strings, with support for placeholder replacements.

#### Scenario: Player lacks permission for command
- **WHEN** a player executes a command or opens a furnace without required permissions
- **THEN** the system sends the formatted `no-permission` or `no-furnace-permission` message from `messages.yml`.

