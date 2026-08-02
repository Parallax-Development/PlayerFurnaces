## 1. Command Registration & Parsing

- [x] 1.1 Register /pfa force-open sub-command in the main command executor.
- [x] 1.2 Implement argument parsing for <player>, <furnaceIndex>, and the optional --bypass-perms flag.
- [x] 1.3 Add validation to ensure the command is executed by the console or a player with admin permissions.

## 2. Validation & Edge Cases

- [x] 2.1 Implement target player validation (must be online and not dead).
- [x] 2.2 Implement furnace index validation (must be within the configured max-furnaces-per-player bounds).
- [x] 2.3 Implement permission checking for the target player on the specified furnace index (unless --bypass-perms is true).

## 3. UI Flow Implementation

- [x] 3.1 Implement synchronous player.closeInventory() call for the target player to clear existing UI state.
- [x] 3.2 Invoke the existing PlayerFurnaces GUI open method for the target player and specified furnace index.
- [x] 3.3 Ensure the furnace opens correctly and the console receives a success message.

## 4. Testing & Verification

- [x] 4.1 Test standard force-open on an online player with permissions.
- [x] 4.2 Test force-open on a player without permissions (expect failure).
- [x] 4.3 Test force-open with --bypass-perms on a player without permissions (expect success).
- [x] 4.4 Test edge cases: target player offline, target player dead, invalid index out of bounds.
