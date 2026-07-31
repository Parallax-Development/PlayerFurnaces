## Why

PlayerFurnaces commands (`/furnace` and `/playerfurnacesadmin`) currently lack tab-completion features, requiring players and administrators to manually type out furnace IDs and administrative subcommands/player names. Adding auto-completion improves player UX, reduces command usage errors, and speeds up admin workflows.

## What Changes

- Implement tab completion for `/furnace [furnaceId]` to suggest accessible furnace IDs.
- Implement tab completion for `/playerfurnacesadmin <subcommand> [player] [furnaceId]` to suggest admin subcommands (`reload`, `view`), online player names, and furnace IDs.
- Filter furnace ID suggestions based on player permissions (`playerfurnaces.furnace.<id>` and `playerfurnaces.admin`).
- Filter string matches dynamically as the sender types.

## Capabilities

### New Capabilities
- `command-tab-completion`: Tab-completion support for player and administrator commands in PlayerFurnaces.

### Modified Capabilities
<!-- None -->

## Impact

- `dev.darkblade.playerfurnaces.command.PlayerFurnaceCommand`: Will implement `TabExecutor` (or `TabCompleter`).
- `dev.darkblade.playerfurnaces.command.AdminCommand`: Will implement `TabExecutor` (or `TabCompleter`).
- `dev.darkblade.playerfurnaces.PlayerFurnacesPlugin`: Will register tab completion when initializing commands.
