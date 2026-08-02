## Context

PlayerFurnaces allows players to open their private furnaces via GUI. Some servers want to automate furnace access using third-party menu plugins (e.g., DeluxeMenus) or console commands given as rewards, but currently, only the player themselves can invoke the command to open a furnace, and it strictly respects permission nodes. A console command is needed to force-open these menus on behalf of the player.

## Goals / Non-Goals

**Goals:**
- Provide a console-executable command to force a target player to open their furnace GUI.
- Allow optional bypassing of standard permissions for the specified furnace index.
- Ensure safe GUI transitions (closing existing GUIs first).

**Non-Goals:**
- Allowing players to run this command to open other players' furnaces (this is strictly a console/admin command).
- Creating furnaces that are beyond the configured maximum limit.

## Decisions

- **Command Syntax:** /pfa force-open <player> <furnaceIndex> [--bypass-perms]. This syntax allows it to be neatly integrated under the existing /pfa admin command structure.
- **Handling Permissions:** By default, the command respects the target player's permissions. This prevents accidental granting of access to locked furnaces. Bypassing requires explicit intent via --bypass-perms.
- **Handling UI State:** We will forcefully call player.closeInventory() synchronously before opening the furnace GUI. This prevents duplication glitches that can occur when a custom GUI is opened while a vanilla container or trading menu is active.
- **Bounds Checking:** If <furnaceIndex> exceeds the maximum allowed furnaces per player as defined in config, the command will abort. This maintains the plugin's integrity and prevents runaway data generation.
- **State Checks:** We will verify player.isOnline() and !player.isDead() before proceeding, failing fast to prevent client-side ghost GUIs or crashes.

## Risks / Trade-offs

- **Risk:** Force-closing a player's inventory might interrupt them while they are actively crafting or trading, potentially dropping items to the floor depending on server configuration.
  - *Mitigation:* This is a known trade-off of server-forced GUI opens. Server admins should be aware of this when designing their automation.
- **Risk:** Third-party plugins like CombatLogX might block GUI opens.
  - *Mitigation:* The orce-open command will invoke the standard PlayerFurnaces GUI open logic, which may or may not be hooked by those plugins. If hooked, it will fail gracefully.
