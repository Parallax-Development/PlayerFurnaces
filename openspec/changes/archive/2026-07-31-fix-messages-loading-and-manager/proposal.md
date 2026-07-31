## Why

Currently, the plugin includes a `messages.yml` resource file in its JAR, but it is never saved to the plugin's data folder on startup (`onEnable()`). Furthermore, the plugin lacks a dedicated `MessageManager` to handle reading, reloading, color formatting (`&`), prefixing, and placeholder substitution for user and admin messages. Commands and GUIs currently use hardcoded Spanish/English strings.

## What Changes

- Add automatic default saving of `messages.yml` on plugin enable if the file does not exist in the data folder.
- Create a dedicated `MessageManager` class responsible for loading, reloading, formatting color codes, and replacing variables in messages.
- Update `messages.yml` with comprehensive configurable message keys for all player and admin interactions.
- Refactor `PlayerFurnaceCommand`, `AdminCommand`, and plugin event listeners to utilize `MessageManager` for all output instead of hardcoded strings.
- Add `messageManager.reloadMessages()` to the `/pfadmin reload` command routine.

## Capabilities

### New Capabilities
- `message-management`: Automatic loading, reloading, and dynamic placeholder/color formatting of configurable messages from `messages.yml`.

### Modified Capabilities

## Impact

- **Affected Code**: `PlayerFurnacesPlugin.java`, `PlayerFurnaceCommand.java`, `AdminCommand.java`, `messages.yml`, and new `MessageManager.java`.
- **APIs/Dependencies**: Standard Bukkit/Spigot `FileConfiguration` and `ChatColor`.
