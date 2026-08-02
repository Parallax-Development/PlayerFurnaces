## Why

Administrators and third-party plugins (like DeluxeMenus or ChestCommands) need a way to forcefully open a player's private furnace GUI from the console. This is essential for server automation, custom rewards menus, and guided tutorials without requiring the player to run the command themselves.

## What Changes

- Add a new sub-command /pfa force-open <player> <furnaceIndex> [--bypass-perms] executable from the console.
- If the --bypass-perms flag is provided, the command ignores the target player's lack of permission for that specific furnace index and opens it anyway.
- If the --bypass-perms flag is NOT provided, and the player lacks permission, the command fails and logs an error to the sender.
- If the <furnaceIndex> is out of the configured limits (e.g. index 99 when max is 10) or invalid, the command strictly fails and logs an error to the sender (no auto-creation beyond bounds).
- If the target player is dead or offline, the command safely fails to prevent client crashes or item duplication.
- Any currently open inventory for the target player is forcefully closed before opening the new furnace GUI to prevent UI glitches.

## Capabilities

### New Capabilities
- orce-open-command: Defines the behavior, arguments, flags, and edge cases of the /pfa force-open command.

### Modified Capabilities

## Impact

- Commands: Addition of a new administrative sub-command and its parsing logic.
- GUI/Inventory Management: Safe closing of existing inventories before forcing a new one open.
- Permissions: Conditional bypassing of permissions when opening furnaces.
