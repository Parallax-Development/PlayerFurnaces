## Context

The `PlayerFurnaces` plugin currently registers `PlayerFurnaceCommand` and `AdminCommand` as `CommandExecutor`s. Neither class implements `TabCompleter` or `TabExecutor`, leaving commands without auto-completion.

## Goals / Non-Goals

**Goals:**
- Implement `TabExecutor` interface on `PlayerFurnaceCommand` and `AdminCommand`.
- Provide contextual, permission-aware tab completion for `/furnace` and `/playerfurnacesadmin` subcommands.
- Filter results using Bukkit `StringUtil.copyPartialMatches`.

**Non-Goals:**
- Introducing third-party command frameworks (e.g. ACF or CommandAPI) for simple command structures.

## Decisions

### Decision 1: Implement `TabExecutor` interface directly on command classes
- **Rationale**: Keeps execution and tab-completion logic unified in the respective command handler class without adding extra boilerplate classes.
- **Alternatives Considered**: Creating separate `TabCompleter` classes. Rejected to keep the codebase compact and straightforward.

### Decision 2: Permission-aware furnace ID suggestions
- **Rationale**: For `/furnace [id]`, query `plugin.getFurnaceManager().hasPermissionForFurnace(player, i)` so players only see furnace IDs they are allowed to open.

### Decision 3: Use `org.bukkit.util.StringUtil.copyPartialMatches`
- **Rationale**: Standard Paper/Bukkit utility for case-insensitive partial match copying into a sorted/filtered list.

## Risks / Trade-offs

- **[Risk]** Non-player senders (e.g. Console) executing `/furnace` or `/playerfurnacesadmin view`.
  - *Mitigation*: Check `if (!(sender instanceof Player))` and return an empty list or appropriate console completions.
